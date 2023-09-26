ИНСТРУКЦИЯ

1. Загружаем папку с проектом на сервер любым удобным способом
2. Устанавливаем на сервер Java sudo apt install openjdk-19-jdk
3. Устанавливаем сборщик maven sudo apt-get install maven
4. Открываем корневую папку с проектом где лежит фаил pom.xml и начинаем сборку  mvn clean package

Далее нам нужно настроить автоматический запуск бота
1. Создаем unit-файл: sudo nano /etc/systemd/system/exchange_bot.service
Вот пример содержимого файла, если проект лежит в корне системы:

[Unit]
Description=Exchange Bot Service
After=network.target

[Service]
ExecStart=/usr/bin/java -jar /exchange_bot-1.0-SNAPSHOT.jar
WorkingDirectory=/exchangeBot/target
User=root
Group=root
Restart=always

[Install]
WantedBy=multi-user.target

2. Перезапускаем службу авто-запуска, что бы она нашла наш unit: sudo systemctl daemon-reload
3. Запускаем бота через службу: sudo systemctl start exchange_bot
4. Включаем в список автозагрузки: sudo systemctl enable exchange_bot
5. Проверим состояние и настройку: sudo systemctl status my_java_app


Если нужно изменить чат, куда отправялем сообщения или привязать к другому боту в ТГ нужно зайти в src/resources application.properties
В нем заменить информацию соответственно.

После изменеия данных в application.properties необходимо:
1. Удалить папку target в корне приложения
2. Пересобрать maven проект
3. Перезапустить бота

Сейчас установлены такие значения:
bot.username=fiveminchange_signal_bot
bot.token=6429965729:AAELxM4XNNfk3SrN_tl3UzsKNvXZPBRN4_E
admin.chat.id=-1001931312329

тестовые данные:
bot.username=cryptoBotTestRube_bot
bot.token=6345313517:AAGb8Oe27Wuid1VMYA2mchrXVt44xgFi1Ww
admin.chat.id=-4038424784