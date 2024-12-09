# Currency Exchange APP

### Wymagania
- Java 17+
- Maven

### Uruchomienie
1. Najpierw musisz sklonować repozytorium z kodem źródłowym aplikacji. Użyj poniższej komendy w terminalu:
```markdown
git clone ścieżka do repo
```
2. Po sklonowaniu repozytorium przejdź do katalogu projektu. Użyj poniższej komendy:
```markdown
cd ścieżka do projektu
```
3. Aby zbudować apliakcję użyj komendy:
```markdown
mvn clean install
```
4. Do uruchomienia użyj komend:
```markdown
mvn spring-boot:run
```
5. Po uruchomieniu aplikacji, powinna być dostępna pod adresem: http://localhost:8080, 
natomiast jej działanie można przetestować na kilka sposobów: curl, Postman oraz poprzez [Swaggera](http://localhost:8080/swagger-ui/index.html),
który zawiera pełną dokumentację udostępnionych endpointów.

