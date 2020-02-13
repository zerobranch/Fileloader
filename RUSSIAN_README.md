# File Loader
[![](https://jitpack.io/v/zerobranch/Fileloader.svg)](https://jitpack.io/#zerobranch/Fileloader)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/zerobranch/Fileloader/blob/master/LICENSE) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Fileloader-green.svg?style=flat)](https://android-arsenal.com/details/1/7216)

Библиотека для управления загрузками файлов на платформе Android

##### Выберите язык
[English](https://github.com/zerobranch/Fileloader/blob/master/README.md) 

[Русский](https://github.com/zerobranch/Fileloader/blob/master/RUSSIAN_README.md)

## Описание
File Loader - это библиотека для платформы android. Она позволяет скачивать любые файлы без особых услилий и получать результат в указанном вами потоке. 

## Возможности
- Скачивать любые файлы по их ссылке
- Сохранять файлы в указанной папке
- Получать результат в виде байтов
- Добавлять файлы в очередь для их дальнейшего скачивания
- Отслеживать все этапы скачивания файлов

## Пример использования

```java
Loader.with(Context)
        .fromUrl("YOUR_URL")
        .load();
```

## Описание методов
**Обязательные**
```
- with(Cntext) - основной метод, в него передается объкт Context
- addInQueue("YOUR_URL") - добавить файл в очередь для загрузки
- load() - начать загрузку файлов
```

**Необязательные**
```
- to("YOUR_PATH") - путь, куда будет загружен файл
- addInQueue("YOUR_URL") - добавить файл в очередь для загрузки
- skipIfFileExist() - загрузка будет прекращена, если файл уже существует (по умолчанию файл перезаписывается)
- abortNextIfError() - если произошла ошибка во время скачивания - прервать остальные загрузки ожидающие в очереди
- makeImmortal() - загрузка файлов будет осуществляться на переднем плане. Загрузка не прекратится даже если её выгрузить из памяти
- notification(Notification) - подключить свое уведомление, которое будет отображаться во время загрузки файлов
- redownloadAttemptCount(4) - количество попыток загрузить файл, в случае если произошла ошибка
- skipCache() - не сохранять файл в памяти устройства
- viewNotificationOnFinish() - не закрывать уведомление после загрузки файлов 
- hideDefaultNotification() - скрывать уведомление при загрузке файлов (не рекомендуется)
- downloadReceiver() - установка DownloadReceiver позволит установить события для получения обратной связь с загрузчика
- onStart(OnStart) - событие начала загрузки очередного файла
- onError(OnError) - событие возникнования ошибки при загрузке файла
- onCompleted(OnCompleted) - событие завершения загрузки очередного файла
- onProgress(OnProgress) - событие для отслеживания хода загрузки
- receivedFile(ReceivedFile) - после завершения загрузки получить путь к загруженному файлу
- receivedFileSource(ReceivedFileSource) - после завершения загрузки получить исходный файл в виде массива байтов
- enableLogging() - включить логирование во время загрузки файлов
```

**Дополнительные методы**
```
loader.cancel() - завершить все загрузки и удалить недозагруженный файл
loader.unsubscribe - отписаться от событий (загрузка не будет прервана)
```

**Примечание**
```
- если не указать to("YOUR_PATH"), то по умолчанию файлы будут сохраняется в кэше приложения - Context.getCacheDir()
- notification(Notification), hideDefaultNotification(), viewNotificationOnFinish() - работают только при установке makeImmortal()
- для установки событий (onStart, onError и т.п.) необходимо установить DownloadReceiver
- при установке собятия receivedFileSource - данные перейдут в этот метод БЕЗ СОХРАНЕНИЯ на устройстве 
```


## Интеграция
Добавьте в корневой build.gradle следующий репозиторий:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Добавьте в build.gradle вашего модуля следующую зависимость:
```groovy
dependencies {
    implementation 'com.github.zerobranch:Fileloader:1.0.0'
}
```

## Лицензия

```
The MIT License (MIT)

Copyright (c) 2017 Arman Sargsyan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
