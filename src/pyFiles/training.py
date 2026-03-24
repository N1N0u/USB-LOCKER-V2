import cv2
import numpy as np
from PIL import Image
import os


def get_images_and_labels(path):
    """
    Load all face images and their IDs from dataset folder
    Returns: face samples (numpy arrays) and corresponding IDs
    """
    # Get all image file paths
    image_paths = [os.path.join(path, f) for f in os.listdir(path)
                   if f.endswith(('.jpg', '.png'))]

    face_samples = []
    ids = []

    # Load Haar Cascade for face detection during training
    detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

    for image_path in image_paths:
        try:
            # Open image and convert to grayscale
            pil_img = Image.open(image_path).convert('L')  # 'L' = grayscale
            img_numpy = np.array(pil_img, 'uint8')

            # Extract ID from filename: User.ID.count.jpg
            # Split by '.' and get the second element (index 1)
            filename = os.path.split(image_path)[-1]
            id = int(filename.split(".")[1])

            # Detect face in the training image (for verification)
            faces = detector.detectMultiScale(img_numpy)

            # If face found, add to training set
            for (x, y, w, h) in faces:
                face_samples.append(img_numpy[y:y + h, x:x + w])
                ids.append(id)
                print(f"[INFO] Loaded: {filename} -> ID: {id}")

        except Exception as e:
            print(f"[WARNING] Error loading {image_path}: {e}")
            continue

    return face_samples, ids


def train_model():
    # Create trainer folder if needed
    os.makedirs("trainer", exist_ok=True)

    # Initialize LBPH face recognizer
    # Parameters you can tune:
    # radius: 1, neighbors: 8, grid_x: 8, grid_y: 8
    recognizer = cv2.face.LBPHFaceRecognizer_create(
        radius=1,
        neighbors=8,
        grid_x=8,
        grid_y=8
    )

    print("[INFO] Loading faces...")
    faces, ids = get_images_and_labels('dataset')

    if len(faces) == 0:
        print("[ERROR] No training images found in 'dataset/' folder")
        return

    print(f"[INFO] Training on {len(faces)} face samples...")

    # Train the model
    recognizer.train(faces, np.array(ids))

    # Save trained model
    recognizer.write('trainer/trainer.yml')
    print(f"[INFO] Model trained and saved to trainer/trainer.yml")
    print(f"[INFO] Unique IDs in model: {set(ids)}")


if __name__ == "__main__":
    train_model()