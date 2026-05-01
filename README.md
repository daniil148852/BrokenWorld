# Oasis Client Mod

Fabric мод для Minecraft 1.21.1 — захватывает кадры игры и отправляет их на AI-сервер (Colab/локальный), получает обратно трансформированное изображение и показывает его как fullscreen оверлей.

## Управление

| Клавиша | Действие |
|---------|----------|
| `V` | Включить/выключить оверлей |
| `R` | Захватить кадр вручную |

Автозахват происходит каждые N секунд (по умолчанию 3) пока оверлей включён.

## Конфиг

Файл: `.minecraft/config/oasis-client.json`

```json
{
  "serverUrl": "http://localhost:5000",
  "prompt": "photorealistic, ultra detailed, 8k",
  "captureIntervalSeconds": 3.0,
  "autoCaptureEnabled": true,
  "overlayOpacity": 1.0,
  "captureWidth": 512,
  "captureHeight": 512
}
```

Поменяй `serverUrl` на URL своего Colab-сервера (через ngrok).

## Сборка

```bash
./gradlew build
```

JAR будет в `build/libs/`.

## Зависимости

- Minecraft 1.21.1
- Fabric Loader >= 0.16.0
- Fabric API

## Как это работает

```
[Minecraft] --кадр PNG--> [Colab/Flask сервер] --img2img AI--> [обратно PNG] --оверлей-->
```

Серверная часть (Colab ноутбук) делается отдельно.
