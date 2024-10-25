let eventSource = null

export function connectSSE() {
  if (!eventSource) {
    // 시큐리티가 적용된 서버에 sse 연결 요청을 위해서는 토큰을 요청해야한다. 
    // eventSource = new EventSource(`/api/notifications/sseconnect`, {
    eventSource = new EventSource(`/boot/api/notifications/sseconnect`, {
      headers: {
        Authorization: `Bearer ${JSON.parse(localStorage.getItem('token')).accessToken}`
      },
      withCredentials: true
    })

    // 알림 이벤트 수신
    eventSource.addEventListener('notification', (event) => {
      console.log('새로운 알림:', event.data)
    })

    // 에러 처리
    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error)
    }
  }

  return eventSource
}

export function closeSSE() {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
}