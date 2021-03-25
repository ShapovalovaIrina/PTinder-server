# Небольшие подсказки по работе с докером

[Установка докера на Ubuntu](https://docs.docker.com/engine/install/ubuntu/)

Докер оперирует образами и контейнерами. Образ по сути как установочный диск, контейнер - запущенная игра. 
Чтобы запустить контейнер надо скачать образ.
#### Команды для образов и контейнеров:
+ _docker images_ - всех доступные на хосте образы (скачанные)
+ _docker container ls_ - все активные контейнеры (с ключом -l выведутся и остановленные контейнеры)
+ _docker rm \<container id\>_ - удаление контейнера 
+ _docker rmi \<image id\>_ - удаление образа
+ _docker pull \<image name\>_ - скачивание образа. Если не указывается хост, то ищется в Docker Hub. Для нашего случая тут может быть docker pull shapovalovairina/ptinder.server

Нам нужно развернуть у себя наш сервер + базу данных. Я написала небольшой docker-compose.yml файл, который запускает это все одной командой.

По итогу у нас на хосте будет два контейнера. И мы сможем обращаться к серверу как обычно. Например curl 192.168.17.135:8080/ptinder/pets/types направит запрос к нам в контейнер. Абсолютно аналогично, как с запуском сервера через идею.

Для развертывания я использовала оркестратор docker swarm. Перед его использованием нужно активировать этот режим командой `docker swarm init`.
Docker swarm уже оперирует сервисами - это группа контейнеров, объединенная одной конфигурацией. У нас это можно воспринимать как все те же единичные контейнеры.
#### Команды для развертывания связанных контейнеров
+ _docker stack deploy -c docker-compose.yml app_ - деплой контейнеров. Ключ -с указывает местонахождения файла конфигурации. App тут просто имя стека. Может быть любым
+ _docker stack rm app_ - соотвественно удаляет развернутый стек
+ _docker stack ps app_ - посмотреть процессы в указанном стеке. Там обычно пишется состояние контейнеров и ошибки, если они падают
+ _docker service ls_ - посмотреть сервисы. Опять же состояние контейнеров

### А что вообще по итогу делать и как запускать
Достаточно просто установить докер, активировать режим swarm. Скачать к себе файлик docker-compose.yml, зайти в папку с ним и запустить команду

`docker stack deploy -c docker-compose.yml app`

Запустить команду `docker service ls` и `docker stack ps app` и убедиться, что ничего не упало

Можно сделать curl запрос к серверу еще