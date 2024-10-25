# MariaDB 기반 이미지 사용
FROM mariadb:latest

# 환경 변수 설정 (루트 비밀번호)
ENV MYSQL_ROOT_PASSWORD=mariadb
ENV MYSQL_DATABASE=yoribogodb
# init.sql에 설정된 비밀번호



# 초기 SQL 파일 복사 (선택 사항)
COPY ./init.sql /docker-entrypoint-initdb.d/

# 컨테이너 시작 시 MariaDB 실행
EXPOSE 3306
