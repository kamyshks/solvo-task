## Solvo-task

### Процесс запуска
1. Выполнить команду `mvn clean package`
2. В директории target запустить исполяемый jar-архив `java -jar solvo-task-1.0`
3. Примеры запуска
```shell
java -jar solvo-task-1.0 -s -l 100 -c 1
java -jar solvo-task-1.0 -g 100,101,102
java -jar solvo-task-1.0 -e export-file
```


Команды для запуска:
Создание N кол-ва грузов в ячейке
-  -s
-  -l,--locationName        название ячейки
-  -c,--countLoads          количество грузов

Вывод общей информации о грузах в ячейках.
-  -g,--locationNames       названия ячеек (Например: 104,55,134)

Экспорт всех данных в xml файл
-  -e,--fileName             название файла

