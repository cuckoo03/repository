elasticsearch 1.x 분석기 플러그인

빌드
>mvn clean install
빌드후 target폴더에 있는 .jar파일의 확장자를 zip으로 변경

설치방법
elasticsearch 설치 서버에 임의의 폴더에 복사하고 플러그인 명령어로 인스톨 실행
>es설치폴더/bin/plugin --url  file:///home/service/es-1-analysis-1.0.0.zip --install 플러그인명
플러그인명=AnalysisMyPlugin.java에 설정한 이름

인스톨 확인
>es설치폴더/bin/plugin --list

플러그인 제거
>es설치폴더/bin/plugin --remove 플러그인명 

