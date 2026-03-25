import cv2
import numpy as np
from PIL import Image
import os


def get_images_and_labels(path):
    image_paths = [os.path.join(path, f) for f in os.listdir(path)
                   if f.endswith(('.jpg', '.png'))]

    face_samples = []
    ids = []

    # ✅ FIXED
    base_dir = os.path.dirname(os.path.abspath(__file__))
    project_src = os.path.dirname(base_dir)
    cascade_path = os.path.join(project_src, "assets", "haarcascade_frontalface_default.xml")

    face_detector = cv2.CascadeClassifier(cascade_path)

    for image_path in image_paths:
        try:
            pil_img = Image.open(image_path).convert('L')
            img_numpy = np.array(pil_img, 'uint8')

            filename = os.path.split(image_path)[-1]
            id = int(filename.split(".")[1])

            # ✅ FIXED
            faces = face_detector.detectMultiScale(img_numpy)

            for (x, y, w, h) in faces:
                face_samples.append(img_numpy[y:y + h, x:x + w])
                ids.append(id)
                print(f"[INFO] Loaded: {filename} -> ID: {id}", flush=True)

        except Exception as e:
            print(f"[WARNING] Error loading {image_path}: {e}", flush=True)

    return face_samples, ids


def train_model():
    os.makedirs("trainer", exist_ok=True)

    recognizer = cv2.face.LBPHFaceRecognizer_create()

    print("[INFO] Loading faces...", flush=True)

    faces, ids = get_images_and_labels('dataset')

    if len(faces) == 0:
        print("[ERROR] No training images found", flush=True)
        return

    print(f"[INFO] Training on {len(faces)} samples...", flush=True)

    recognizer.train(faces, np.array(ids))
    recognizer.write('trainer/trainer.yml')

    print("[INFO] Training completed", flush=True)


if __name__ == "__main__":
    train_model()