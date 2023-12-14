# Instrukcja stawiania

1. Ustawiamy env.sh zgodnie ze swoim środowiskiem
2. Ustawiamy application.proporties zgodnie ze swoim środowiskiem
3. (Opcjonalnie jak nie mamy) Instalujemy wget'a i grunt

### Backend

1. Kompilujemy mavenem - zalecane dodać sobie do konfiguracji Intellij
``
    $ mvn clean package
``	
2. Ustawiamy backend/target/generated-sources/java jako generated source root w Intellij
3. Backend odpalamy springową konfiguracja która nam się zaimportowała, ale pierwszy raz dopiero po stworzeniu bazy - patrz następny punkt

### Baza

1. Wykonujemy backend/etc/scripts/create_database.sh
2. Następnie backend/etc/scripts/start_database.sh
3. Startujemy backend aplikacji
4. (Opcjonalnie) Logujemy się do bazy ./baza.sh, aby wgrać dane

### Frontend

1. Dodajemy do intellij konfiguracje dla npm. wybieramy command: run, scripts: serve oraz wskazujemy plik package.json
2. Ustawiamy .env.development.local application.proporties zgodnie ze swoim środowiskiem
3. W katalogu 'frontend' wykonujemy
``
    $ npm install && npm audit fix -f
``
4. TODO: napewno coś z gruntem itp.
   ``
   $ grunt vue
   ``
5. Odpalamy konfiguracje npm'a. Na frontend wchodzimy http://localhost:8080
