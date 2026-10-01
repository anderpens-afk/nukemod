Nuke Mod (Fabric 1.20.1): ядерная бомба + пульсар

Сборка (нужны JDK 17 и интернет):
  gradle wrapper    # один раз, если нет gradlew
  ./gradlew build   # на Windows: gradlew.bat build
Готовый мод: build/libs/nukemod-1.0.0.jar
Положи его в папку mods вместе с Fabric Loader и Fabric API 0.92.2.

В игре:
  /give @s nukemod:pulsar      - пульсар (поставь и отойди)
  /give @s nukemod:nuke_block  - ядерная бомба
Редстоун-сигнал выключает пульсар. Сломай блок - остановится.

Настройки в PulsarBlockEntity.java: LENGTH, CUT_BLOCKS, CONE, SPIN.
CUT_BLOCKS = false - только эффекты, мир не ломается.

Сборка без установки (GitHub):
  1. Создай репозиторий на github.com и загрузи в него все файлы проекта
     (включая папку .github).
  2. Открой вкладку Actions, дождись зелёной галочки (2-5 минут).
  3. Открой завершённый запуск, внизу в Artifacts скачай nukemod-jar.
  4. Внутри архива два файла: бери nukemod-1.0.0.jar (не sources) и клади в mods.
