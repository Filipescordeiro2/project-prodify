import requests
import json
import random

def generate_products():
    product_names = [
        "Apple iPhone 13", "Samsung Galaxy S21", "Sony WH-1000XM4 Headphones", "Dell XPS 13 Laptop",
        "Apple MacBook Pro", "Google Pixel 6", "Bose QuietComfort 35 II", "Nintendo Switch",
        "Sony PlayStation 5", "Microsoft Xbox Series X", "HP Spectre x360", "Asus ROG Zephyrus G14",
        "Lenovo ThinkPad X1 Carbon", "Acer Predator Helios 300", "Razer Blade 15", "LG OLED TV",
        "Samsung QLED TV", "Sony Bravia TV", "Apple iPad Pro", "Samsung Galaxy Tab S7",
        "Amazon Kindle Paperwhite", "Microsoft Surface Pro 7", "Google Nest Hub", "Amazon Echo Dot",
        "Apple AirPods Pro", "Samsung Galaxy Buds Pro", "Fitbit Charge 4", "Garmin Forerunner 245",
        "Apple Watch Series 6", "Samsung Galaxy Watch 3", "Canon EOS R5", "Nikon Z6 II",
        "Sony Alpha a7 III", "GoPro HERO9", "DJI Mavic Air 2", "Ring Video Doorbell 3", "Nest Learning Thermostat",
        "Philips Hue Smart Bulbs", "TP-Link Kasa Smart Plug", "August Smart Lock", "Tile Mate",
        "Anker PowerCore 10000", "Roku Streaming Stick+", "Chromecast with Google TV", "Fire TV Stick 4K",
        "NVIDIA Shield TV", "Apple TV 4K", "PlayStation VR", "Oculus Quest 2", "HTC Vive Cosmos",
        "Valve Index", "Corsair K95 RGB Platinum", "Razer DeathAdder V2", "Logitech MX Master 3",
        "SteelSeries Arctis 7", "HyperX Cloud II", "Blue Yeti USB Microphone", "Elgato Stream Deck",
        "Acer Nitro XV272U", "Dell UltraSharp U2720Q", "LG UltraGear 27GL850", "Samsung Odyssey G7",
        "BenQ PD3220U", "MSI Optix MAG272CQR", "Gigabyte Aorus FI27Q", "Alienware AW3420DW",
        "Apple Magic Keyboard", "Microsoft Sculpt Ergonomic Keyboard", "Logitech G Pro X",
        "Razer BlackWidow Elite", "Corsair HS70 Pro", "SteelSeries Rival 600", "HyperX Alloy FPS Pro",
        "Logitech G502 Hero", "Razer Naga Trinity", "Corsair Dark Core RGB Pro", "SteelSeries Apex Pro",
        "HyperX Pulsefire FPS Pro", "Logitech G903", "Razer Basilisk Ultimate", "Corsair Scimitar RGB Elite",
        "SteelSeries Sensei Ten", "HyperX Cloud Stinger", "Logitech G733", "Razer Kraken X",
        "Corsair Virtuoso RGB Wireless", "SteelSeries Arctis Pro", "HyperX Cloud Alpha", "Logitech G935",
        "Razer Thresher Ultimate", "Corsair Void RGB Elite", "SteelSeries Arctis 1 Wireless", "HyperX Cloud Flight",
        "Logitech G433", "Razer Tiamat 7.1 V2", "Corsair HS60 Haptic", "SteelSeries Arctis 5", "HyperX Cloud Revolver S"
    ]

    products = []
    for name in product_names:
        price = round(random.uniform(50, 1500), 2)
        stock = random.randint(10, 200)
        products.append({"name": name, "price": price, "stock": stock})

    return products

def send_product(product):
    url = "http://localhost:8080/product"
    headers = {"Content-Type": "application/json"}
    response = requests.post(url, data=json.dumps(product), headers=headers)
    return response.status_code, response.json()

def main():
    products = generate_products()
    for product in products:
        status_code, response = send_product(product)
        print(f"Product: {product['name']}, Status Code: {status_code}, Response: {response}")

if __name__ == "__main__":
    main()