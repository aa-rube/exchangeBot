ИНСТРУКЦИЯ

1. Загружаем папку на с проектом на сервер любым удобным способом
2. Устанавливаем на сервер Java командой sudo apt install openjdk-19-jdk
3. Устанавливаем сборшик maven sudo apt-get install maven

4. Открываем корневую папку с проектом где лежит фаил pom.xml 
и начинаем сборку  mvn clean package



Далее нам нужно настроить автоматический запуск после перезагрузки сервера

1.

bot.username=fiveminchange_signal_bot
bot.token=6429965729:AAELxM4XNNfk3SrN_tl3UzsKNvXZPBRN4_E
admin.chat.id=-1001931312329