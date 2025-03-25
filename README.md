# areus_spring README


### Szükséges környezet

**JDK 21** verzió telepítése szükséges. Használt JDK Oracle: (`jdk-21.0.5_linux-x64_bin`).

Fejlesztés IntelliJ IDEA Community Edition

Teszt adatbázis MySql. Paraméterek:

```
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test
spring.datasource.username=root
spring.datasource.password=
```
## Build és Futtatás


A projekt felépítése és futtatása:

bash
```
   ./mvnw clean install
   ./mvnw spring-boot:run
```
   
A projekt alapértelmezett portja: localhost:8080

## API Endpointok
Basic Auth szükséges.
(username:admin, password:admin123)
# POST /customers

Új ügyfél létrehozása.
# GET /customers

Minden ügyfél lekérdezése.
# GET /customers/{id}

Egy ügyfél adatainak lekérése az id alapján.
# PUT /customers/{id}

Ügyfél teljes frissítése. Az összes adatot meg kell adni.
# PATCH /customers/{id}

Ügyfél adatainak részleges frissítése. Csak a módosítani kívánt adatokat kell megadni.
# DELETE /customers/{id}

Ügyfél törlése az id alapján.
# GET /customers/average-age

Az összes ügyfél átlagos életkorának lekérdezése.
# GET /customers/age-young

A 18 és 40 év közötti ügyfelek listájának lekérése.
