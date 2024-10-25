<template>
  <div class="wiki-detail-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'wiki'">
      <BackButton></BackButton>
      <div class="menu-name-wrapper">
        <span class="menu-name">{{ menuInfo.menu_name }}</span>
      </div>
      <div class="menu-img-wrapper">
        <img v-show="isImageLoading" :src="defaultImage" alt="MenuImage" />
        <img
          v-show="!isImageLoading"
          :src="menuImageSrc"
          alt="MenuImage"
          @load="handleImageLoad"
          @error="handleImageError"
        />
      </div>
      <div class="menu-ingredient-container">
        <div class="info">재료</div>
        <span class="menu-ingredient">{{ menuInfo.menu_ingredient }}</span>
      </div>
      <div class="menu-manual-container">
        <div class="info">조리 순서</div>
        <RecipeManual
          v-for="manual in manualList"
          :key="manual.manual_id"
          :manualContent="manual.manual_content"
          :manualImage="manual.manual_image"
        ></RecipeManual>
      </div>
    </MainBoard>
    <aside>
      <GoTopButton></GoTopButton>
    </aside>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import BackButton from '@/components/common/BackButton.vue'
import RecipeManual from '@/components/recipe/RecipeManual.vue'
import GoTopButton from '@/components/common/GoTopButton.vue'
import axios from 'axios'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const isImageLoading = ref(true)
const isImageError = ref(false)
const isLogin = ref(false)

const route = useRoute()

const menuInfo = ref({})
const manualList = ref([])
const menuImageSrc = ref('')

const defaultImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)

const fetchData = async () => {
  try {
    console.log("Fetching recipe data..."); // 로그 추가
    const recipeResponse = (await axios.get(`/boot/api/recipes/${route.params.recipeId}`)).data
    console.log("Recipe data:", recipeResponse); // 받아온 데이터 로그
    if (recipeResponse.success) {
      menuInfo.value = recipeResponse.data
      menuImageSrc.value = menuInfo.value.menu_image || defaultImage.value
      console.log("Fetching manual data..."); // 로그 추가
      const manualResponse = (await axios.get(`/boot/api/manuals?recipe=${route.params.recipeId}`)).data
      console.log("Manual data:", manualResponse); // 받아온 데이터 로그
      if (manualResponse.success) {
        manualList.value = manualResponse.data
      } else {
        // Fetch the manual using the Fetch API for streaming response
        const response = await fetch(`/boot/api/manuals/ai?recipe=${route.params.recipeId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            menu_name: menuInfo.value.menu_name,
            menu_ingredient: menuInfo.value.menu_ingredient
          })
        })

        const data = response.body
        const reader = data?.getReader()
        const decoder = new TextDecoder('utf-8');
        const maxLength = 6
        let done = false
        let lastMessage = ''
        let text = ''

        while (!done && reader) {
          const { value, done: doneReading } = await reader.read()
          done = doneReading
          const chunkValue = decoder.decode(value)
          lastMessage += chunkValue
          console.log("Chunk received:", chunkValue); // 매번 받은 청크 데이터 로그
          text = lastMessage.replaceAll('data:', '').replace(/\n/g, '')
          console.log("Processed text:", text); // 가공된 텍스트 로그
          // 공백만 있을 경우
          if (text.trim() === '') {
            lastMessage = ''
            continue
          }

          // 문장이 문자.으로 끝날 경우
          if (/[a-zA-Z가-힣]\.$/.test(text)) {
            console.log("Complete sentence:", text); // 완성된 문장 로그
            manualList.value[manualList.value.length - 1].manual_content = text.trim()
            lastMessage = ''
            text = ''
            // 6개 이하일 때 새로운 빈 항목 추가
            if (manualList.value.length < maxLength) {
              manualList.value.push({ manual_content: '', manual_image: null })
            }
          }
          // 이외의 경우 (수집 중인 문장이므로 마지막 항목 업데이트)
          else {
            console.log("Appending incomplete sentence:", text); // 미완성 문장 로그
            if (manualList.value.length > 0) {
              manualList.value[manualList.value.length - 1].manual_content = text.trim()
            } else {
              manualList.value.push({ manual_content: text.trim(), manual_image: null })
            }
          }
        }

        // 마지막 text가 문자.으로 끝나지 않을 경우에도 처리
        if (text !== '') {
          console.log("Final text update:", text); // 마지막 업데이트 로그
          manualList.value[manualList.value.length - 1].manual_content = text.trim()
        }
      }
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 이미지 로딩 오류 처리 함수
const handleImageError = () => {
  menuImageSrc.value = defaultImage.value
  isImageLoading.value = false // 로딩 중지
  isImageError.value = true
}

// 이미지 로드 완료 처리 함수
const handleImageLoad = () => {
  isImageLoading.value = false // 로딩 중지
  isImageError.value = false
}

let eventSource = null

const handleImageUpdate = (event) => {
  menuImageSrc.value = event.data
  if (eventSource) {
    eventSource.removeEventListener('image-update', handleImageUpdate)
    eventSource.close() // 연결 종료
  }
}

onMounted(async () => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  }
  await fetchData()
  // SSE 연결 생성
  eventSource = new EventSource('/boot/api/notifications/sseconnect')
  eventSource.addEventListener('image-update', handleImageUpdate)
})

</script>

<style scoped>
.wiki-detail-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 20rem;
}

.back-btn {
  position: absolute;
  top: 14rem;
  left: 12rem;
}

.go-top-btn {
  position: fixed;
  top: 84%;
  right: 8rem;
}

.menu-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 18rem;
}

.menu-name {
  font-size: 4rem;
  font-weight: 500;
  color: var(--black-color);
}

.menu-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 72rem;
  height: 54rem;
  overflow: hidden;
  margin-top: 4rem;
  border-radius: 1.4rem;
}

.menu-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.menu-ingredient-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 4rem;
  margin-top: 6rem;
}

.menu-ingredient {
  text-align: center;
  width: 68%;
  font-size: 2.4rem;
  font-weight: 500;
  line-height: 4rem;
  word-wrap: break-word;
}

.menu-manual-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 4rem;
  margin-top: 4rem;
  margin-bottom: 12rem;
}

.info {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 2rem;
  font-size: 2.4rem;
  font-weight: 350;
  color: var(--white-color);
  background-color: var(--black-color);
  border-radius: 3rem;
}

@media screen and (max-width: 1440px) {
  .menu-img-wrapper {
    width: 68rem;
    height: 51rem;
  }

  .back-btn {
    left: 8rem;
  }
}

@media screen and (max-width: 1280px) {
  .menu-img-wrapper {
    width: 52rem;
    height: 39rem;
  }

  .back-btn {
    left: 6rem;
  }
}

@media screen and (max-width: 1024px) {
  .go-top-btn {
    right: 5rem;
  }
}

@media screen and (max-width: 600px) {
  .menu-img-wrapper {
    width: 40rem;
    height: 30rem;
  }

  .back-btn {
    left: 5rem;
  }

  .go-top-btn {
    right: 4rem;
  }

  .menu-name {
    font-size: 3rem;
  }
}

@media screen and (max-width: 480px) {
  .notification-btn {
    left: 10rem;
  }

  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .menu-img-wrapper {
    width: 36rem;
    height: 27rem;
  }

  .notification-btn {
    left: 9rem;
  }

  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .notification-btn {
    left: 8rem;
  }

  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
</style>
