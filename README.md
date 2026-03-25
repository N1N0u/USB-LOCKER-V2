# 🔐 Face Recognition USB Locker (Java + Python)

A hybrid security system that **locks/unlocks USB drives using face recognition**.

This project combines:

* ☕ Java (GUI + USB Control)
* 🧠 Python (OpenCV Face Recognition)
* 📱 DroidCam (IP Camera)

---

## 🚀 Features

* 🎥 Capture face dataset using phone camera (DroidCam)
* 🧠 Train face recognition model (LBPH)
* 👁️ Real-time face recognition
* 🔐 Automatically lock USB drives when inserted
* 🔓 Unlock USB only after successful face authentication
* 🖥️ Simple Swing GUI
* ⚡ Optimized for low-resource environments (CPU-only, no GPU required)

---

## 📸 Demo

### 📷 Data Collection

![Data Collection](data.gif)

### 🧠 Training

![Training](train.gif)

### 👁️ Recognition & Unlock

![Recognition](recognition.gif)

> Full demo available in `demo.mp4`

---

## 🏗️ Project Architecture

### 🔹 Java Side

* Detect USB drives
* Lock USB automatically
* Launch Python scripts
* Read recognition result (`AUTHORIZED`)

---

### 🔹 Python Side

* Face dataset collection
* Model training (LBPH)
* Real-time recognition

---

## ⚙️ Requirements

### 🐍 Python

```bash
pip install opencv-contrib-python
pip install pillow
pip install numpy
```

---

### ☕ Java

* JDK 8+
* Swing

---

## 📱 Camera Setup (DroidCam)

This project uses **DroidCam** as IP camera.

Example stream:

```text
http://YOUR_IP:4747/video
```

Make sure:

* Phone and PC are on same network
* DroidCam is running
* URL works in browser

---

## ▶️ How It Works

### 1. 📸 Recon (Collect Faces)

* Click **Recon**
* System captures ~30 images from camera
* Saves in:

```text
dataset/
```

---

### 2. 🧠 Train Model

* Click **Train**
* Model saved in:

```text
trainer/trainer.yml
```

---

### 3. 🔐 USB Protection

* When USB is inserted:

  * Automatically detected
  * Immediately locked (readonly)

---

### 4. 👁️ Face Recognition

* Click **Disable Protection**
* Python starts recognition
* If face is valid:

```text
AUTHORIZED
```

---

### 5. 🔓 Unlock USB

* Java reads `AUTHORIZED`
* Executes unlock script
* USB becomes accessible

---

## 🧠 Technical Highlights

* LBPH algorithm for efficient face recognition
* CPU-only execution (no GPU required)
* Java ↔ Python communication via process output
* Real-time USB monitoring

---

## 💻 System Specs (Tested On)

> ⚠️ This project was built and tested on a very low-end machine:

* CPU: Intel Core 2 Quad Q8300 @ 2.5GHz
* RAM: 4GB
* GPU: None (CPU-only, no VRAM)

✔️ Despite that, system runs successfully

---

## ⚠️ Important Notes

* Requires **Administrator privileges** (for `diskpart`)
* Works best with:

  * Good lighting
  * Stable camera connection
* USB locking uses `.bat` scripts + PowerShell

---

## 💡 Future Improvements

* Multi-user recognition
* Auto-lock when face disappears
* Better UI feedback
* Remove `.bat` → direct Java control
* Logging system

---

## 📌 Author

ATEF Aliat

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
