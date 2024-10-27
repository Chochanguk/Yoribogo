<template>
  <div class="pagination">
    <button class="prev" @click="prevPage" :disabled="currentPage <= 1">
      <i class="fa-solid fa-caret-left"></i>
    </button>
    <span>{{ currentPage }} / {{ totalPages }}</span>
    <button class="next" @click="nextPage" :disabled="currentPage >= totalPages">
      <i class="fa-solid fa-caret-right"></i>
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

const emit = defineEmits(['changePage'])

// 페이지 계산을 위한 유효성 검사
const totalPages = computed(() => {
  return (props.data && props.data.totalPages > 0) ? props.data.totalPages : 1 // 최소 1 페이지로 설정
})

const currentPage = computed(() => {
  // props.data가 정의되어 있는 경우에만 접근
  return (props.data && props.data.number >= 0) ? props.data.number + 1 : 1; // 현재 페이지 (0부터 시작하므로 +1)
});

// 페이지 이동 함수
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    emit('changePage', currentPage.value + 1) // 페이지를 변경하라는 이벤트를 emit
  }
}

const prevPage = () => {
  if (currentPage.value > 1) {
    emit('changePage', currentPage.value - 1) // 페이지를 변경하라는 이벤트를 emit
  }
}
</script>

<style>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  flex-wrap: nowrap;
  width: 100%;
  margin-bottom: 5rem;
  color: var(--red-color);
}

.prev,
.next {
  transform: translateY(0.3rem);
  border: none;
  background-color: transparent;
  color: var(--red-color);
  cursor: pointer;
}

.prev:disabled,
.next:disabled {
  cursor: text;
}

.pagination i {
  font-size: 3.5rem;
}

.pagination span {
  font-size: 3rem;
  margin-left: 1rem;
  margin-right: 1rem;
}
</style>
