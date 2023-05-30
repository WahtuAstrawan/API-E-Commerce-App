# <h1 align="center">API E-Commerce</h1>
<p align="center">by Wahtu Astrawan</p>

# *Pengenalan*
Selamat datang di program API E-Commerce<br>
Pada program API ini kita dapat melakukan request Method GET, POST, DELETE, dan PUT
Untuk request body dari method POST dan PUT harap menggunakan format JSON
Untuk response dari API ini juga akan dikirimkan dengan format JSON.


# *Alur Program*
### Method GET
Request Method GET digunakan untuk mendapatkan data dari database. Request Method GET yang dapat digunakan pada API ini dapat dilihat sebagai berikut.
<br>

####  1. Mendapatkan data semua user 
Untuk mendapatkan semua user yang ada, dapat menggunakan url seperti berikut.
 http://localhost:8071/users <br>
Hasil eksekusi url diatas dapat dilihat sebagai berikut.
 ```
 [
    {
        "id": 1,
        "first_name": "wahtu ganteng",
        "last_name": "haha",
        "email": "wahtu@gmail",
        "phone_number": "0812781",
        "type": "seller",
        "addresses": []
    },
    {
        "id": 2,
        "first_name": "baim",
        "last_name": "keren",
        "email": "baim@gmail",
        "phone_number": "0912781",
        "type": "buyer",
        "addresses": []
    },
    {
        "id": 3,
        "first_name": "devta",
        "last_name": "mantap",
        "email": "mantap@gmail",
        "phone_number": "0912781",
        "type": "buyer",
        "addresses": []
    },
    {
        "id": 5,
        "first_name": "tude",
        "last_name": "jago basket",
        "email": "tude@gmail",
        "phone_number": "0912781",
        "type": "seller",
        "addresses": []
    }
] 
```
<br>

#### 2) Mendapatkan data detail user berdasarkan id</p>
Untuk mendapatkan data user detail (beserta addressnya) berdasarkan id, dapat menggunakan url sebagai berikut.

 http://localhost:8071/users/{id} 

Hasil eksekusi dengan id 2 dapat dilihat sebagai berikut.

```
{
    "id": 2,
    "first_name": "baim",
    "last_name": "keren",
    "email": "baim@gmail",
    "phone_number": "0912781",
    "type": "buyer",
    "addresses": [
        {
            "id_user": 2,
            "type": "buyer",
            "line1": "Dirumah",
            "line2": "ayang",
            "city": "Kuta",
            "province": "Bali",
            "postcode": "80802"
        }
    ]
}
```

#### 3) Mendapatkan produk berdasarkan id user</p>
Untuk mendapatkan produk berdasarkan id, dapat menggunakan url sebagai berikut.

http://localhost:8071/users/{id}/products

Hasil eksekusi dengan id 1 dapat dilihat sebagai berikut : 
```
[
    {
        "id": 1,
        "id_seller": 1,
        "title": "Product Title",
        "description": "Product Description",
        "price": "100.00",
        "stock": 10
    }
]
 ```

#### 4) Mendapatkan order berdasarkan id user
Untuk mendapatkan order berdasarkan id, dapat menggunakan url sebagai berikut.
 http://localhost:8071/users/{id}/orders

Hasil eksekusi dengan id 5 dari url diatas, dapat dilihat sebagai berikut.
```
[
    {
        "id": 1,
        "id_buyer": 5,
        "note": 1,
        "total": 10,
        "discount": 0,
        "is_paid": false
    }
]
```

#### 5) Mendapatkan review berdasarkan id user
Untuk mendapatkan review berdasarkan id, dapat menggunakan url sebagai berikut.
 http://localhost:8071/users/{id}/reviews

Hasil eksekusi dengan id 1 dapat dilihat sebagai berikut.

```
{
    "id_order": 1,
    "star": 5,
    "description": "Halo ges"
}
```

#### 6) Mendapatkan order yang dengan informasi informasi order, buyer, order detail, review, produk: title,
price berdasarkan id

Untuk mendapatkan data diatas kita dapat menggunakan url sebagai berikut
http://localhost:8071/orders/{id}

Hasil eksekusi dengan id 1 dapat dilihat sebagai berikut.
```
[
    {
        "note": 1,
        "total": 10,
        "reviews": [
            {
                "star": 5,
                "description": "Halo ges"
            }
        ],
        "discount": 0,
        "id": 1,
        "is_paid": "0",
        "order_detail": [],
        "id_buyer": 5
    }
]
```
#### 7) Melihat semua daftar produk ada

Untuk melihat semua daftar produk kita dapat menggunakan url berikut :
http://localhost:8071/products

Hasil eksekusi dapat dilihat sebagai berikut :

```
[
    {
        "id": 1,
        "id_seller": 1,
        "title": "Product Title",
        "description": "Product Description",
        "price": "100.00",
        "stock": 10
    }
]
```

#### 7) Melihat produk dan user berdasarkan id
Untuk melihat produk dan user dari id kita dapat menggunakan url berikut :
http://localhost:8071/products/{id}

Hasil eksekusi dengan id 1 adalah sebagai berikut.
agsl
```
{
    "id": 1,
    "id_seller": 1,
    "title": "Product Title",
    "description": "Product Description",
    "price": "100.00",
    "stock": 10
}{
    "id": 1,
    "first_name": "wahtu ganteng",
    "last_name": "haha",
    "email": "wahtu@gmail",
    "phone_number": "0812781",
    "type": "seller",
    "addresses": []
}
```

<br><h3> Request Method POST </h3>
Request Method POST digunakan untuk menambah record baru ke dalam sebuah tabel. Untuk menggunakan Request Method POST pada API ini, bisa mengirimkan url seperti pada contoh berikut.

 http://localhost:8071/users 

File JSON :

agsl
```
{
    "id": 6,
    "first_name": "tude", 
    "last_name": "jago basket",
    "email":"tude@gmail",
    "phone_number": "0912781",
    "type": "seller"
}
```

Pada contoh diatas, '/users' dan file JSON dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan response seperti berikut :
```
{
    "message": "Data berhasil ditambahkan ke database"
}
```

<br><h3> Request Method PUT </h3>
Request Method PUT digunakan untuk mengubah data yang ada pada tabel. Untuk menggunakan Request Method PUT pada API ini, bisa mengirimkan url seperti pada contoh berikut.

 http://localhost:8071/users/{id}

File JSON :
```
{
    "id": 1,
    "first_name": "wahtu ganteng", 
    "last_name": "haha",
    "email":"wahtu@gmail",
    "phone_number": "0812781",
    "type": "seller"
}
```

Pada contoh diatas '/users', /id, dan file JSON dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output sebagai berikut :
```
{
    "message": "Berhasil mengupdate data users dengan ID : 5"
}
```

<br><h3> Request Method DELETE </h3>
Request Method DELETE digunakan untuk menghapus record yang ada pada sebuah tabel. Untuk menggunakan Request Method DELETE pada API ini, bisa mengirimkan url seperti pada contoh berikut.

 http://localhost:8071/users/{id}

Pada contoh diatas, '/users' dan /id dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output sebagai berikut :
```
{
    "message": "Berhasil menghapus data users dengan ID : 6"
}
```
