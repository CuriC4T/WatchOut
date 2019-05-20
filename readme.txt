capture가 프레임으로 캡쳐하는 버전

webcam_udp가 바로 udp로 보내는 시험용 버전

capture는 모두 라즈베리에 넣어서 컴파일 후 실행

webcam_udp는 서버랑 raspistill 폴더에 있는거 옮겨서 실행

클라이언트(노트북)은 webcam_udp client 동일

컴파일 & 실행 명령어

javac -d . *.java
java -cp . 패키지명폴더.메인있는 소스코드(Main)

자세한건 구글링 



노트북 - 라즈베리 파일 이동법
putty 설치
pscp 명령어 사용

ex)
라즈->PC
pscp pi@172.30.1.29:/home/pi/temp/capture/* c:\git\
pc->라즈
pscp c:\git\* pi@172.30.1.29:/home/pi/temp/capture/

구글링 참고