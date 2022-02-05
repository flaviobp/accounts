# accounts
Curso de verão do IME - Introdução a Microsserviços com SpringBoot

## subir infra
docker-compose up -d

## criar database
docker exec -it  <ID> psql -c "CREATE DATABASE accounts"

## permissoes para root
docker exec -it  <ID> psql -c "GRANT ALL PRIVILEGES ON DATABASE accounts TO root"

## enviar post para criar conta
http POST :8081/accounts < account.json

## create
Get-Content account.json | http POST :8081/accounts

## update
Get-Content account_update.json | http PUT :8081/accounts/1

## delete
http DELETE :8081/accounts/1

## Kafdrop
localhost:19000

