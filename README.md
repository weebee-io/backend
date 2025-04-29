### 커밋 메세지

- feat: : 새로운 기능 추가/수정/삭제
- refactor : 버그를 수정하거나 기능 추가가 없는 단순 코드 변경
- fix: 버그, 오류 해결
- test: : 테스트 코드
- chore: 빌드 업무 수정, settings.py 수정 등
- docs: Readme를 비롯한 문서 변경시
- init: initial commit을 할 시
- build: 라이브러리 추가 등
- !HOTFIX: 치명적 버그를 급하게 수정할 때

### 네이밍 규칙

- class: Pascal ex) MyClass, PersonInfo
- Variable: Snake ex) user_name, total_count
- Function: Snake ex) calculate_total, get_user_data
- Constant : Pascal + Snake pascal ex) MAX_SIZE, DEFAULT_TIMEOUT

### 주석

- Docstring을 활용하여 클래스와 함수단위에 설명을 적어주도록 하자.

```java
def delete_post(post_id):
    """
    모든게시판의 Delete를 담당하는 view
    """
```
