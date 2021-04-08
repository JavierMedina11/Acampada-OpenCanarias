# Canarias Camping

[Lumen](https://lumen.laravel.com/) Laravel Website. \
[Kotlin](https://kotlinlang.org/) Kotlin Website.

## First of all
Download this repository, has a frontend and backend folder

## Backend-Laravel
Go to the backend folder with your favourite ide and type in terminal
```
composer install
```

In your DB gestor create a new database for this project.
```
create database DBNAME;
```

Rename ".env.example" to .env and change some things in archive like you have in your DB
```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=DBNAME
DB_USERNAME=USERNAME
DB_PASSWORD=USERPSWD
```
### To migrate Database
```
php artisan migrate
```

This is for start the local server in your PC
```
php -S yourIp:8000 -t ./public
```

If you want to use reports must do this changes, go to routes/api
and  in aprox line 80
```
'db_connection' => [
            'driver' => 'mysql', //mysql, ....
            'username' => 'YOURDBUSERNAME',
            'password' => 'YOURDBPASSWORD',
            'host' => '127.0.0.1',
            'database' => 'YOURDBNAME',
            'port' => '3306'
        ]
```

## Frontend

Go to the frontend folder and open it with your IDE, must to go to the following route\
and make some changes.
```
app/java/com.example.frontend.controller 
io.ServiceSingleton
```
The change must to change is baseUrl with youp IpAdress:8000/api/

Another change is in route
```
app/java/com.example.frontend.controller 
ui.CreateReservaActivity
```
Near Line 33 in getZone() change url with youp IpAdress:8000/img/

Another change is in route
```
app/java/com.example.frontend.controller 
ui.WebView
```
In line 14, change the url with youp IpAdress:8000/help

### Final Steps
Once in the backend folder your server is ON, you can press the play ▶️ button (AndroidStudio)
and the emulator starts running

## Data model
The data model follows the following description or structure:

* **Operarios Table:** This table will be where we find the operarios.
  * **Id**
  * **Dni**
  * **Nombre**
  * **Email**
  * **Password**
  
* **Zones Table:** This table will be where we find the data that the users of the zones see.
  * **Id:** Zone id
  * **Nombre:** Zone name
  * **Localization:** Zone localization
  * **Url_img:** Zone image
  
* **Bookings Table:** This table will be where we will find the data that you will have to give to develop the reservation.
  * **id_persona:** Id person (FK room table)
  * **Check_out** Check out
  * **Check_in:** Check in
  * **Localizador_reserva** Id
  * **num_personas** Number of persons
  * **num_vehiculos** Number of vehicules
  * **checkin** Id of the reserved room (FK room table)
  * **fecha_checkin** Date of the checkin
  * **id_zona** Id of the reserved zone (FK room table)

* **Persons Table:** This table will be where we will find the data of persons.
  * **id:** Id person
  * **Nombre** Check out
  * **Apellidos:** Check in
  * **Dni** Id
  * **Url_img** Zone person

### Entity Relationship
![Image text](https://github.com/TeteV/AcampadasOC/blob/develop/Docs/utils/ER.PNG)

### Model Relationship
![Image text](https://github.com/TeteV/AcampadasOC/blob/develop/Docs/utils/Modelo%20de%20datos.PNG)

## User requirements
The user requirements can be defined as follows:
* **R1.** Platform.
    * **R1.1.** The app that we will make will be for Mobile.
    * **R1.2.** There is no previous application to take references.
* **R2.** Only the functions that are broken down from the three cruds performed in the app will be implemented.
* **R3.** Users will have to access by registering, providing their name, dni email and password.
* **R4.** Once inside the main activity, they will be able to direct the different ones.

  * **R4.1.** User can access QR viewer.
  * **R4.2.** The user can access the user's profile by clicking on the upper right image.
  * **R4.3.** If you click on any of the zones you will go to list it in detail.
* **R5.** Once inside the part of the user's profile you can contemplate the following possibilities:
  * **R5.2.** The user will be able to logout, by clicking on the orange buttont.
  * **R5.2.** If you click on the one in the blue button, you can go to the user's edition.
  * **R5.3.** If you click on the grey button you will get a report.  
  * **R5.4.** If you click on the grey button you will delete the user.
* **R6.** Once inside the sale of the list, you will be able to:
  * **R6.1.** filter by locator with top input-
  * **R6.2.** You will be able to filter date by the lower input
  * **R6.3.** If you click on a reservation you will see the reservations in detail.
  * **R6.4.** You can filter date by lower input
* **R7.** Once inside the sale of the detailed reservation, you will be able to:
  * **R7.1.** See the data in more detail
  * **R7.2.** Delete said reservation
  * **R7.3.** You can update it with new data from the form that appears.
  * **R7.4.** You can check-in.

## User manual
[User Manual](https://github.com/TeteV/AcampadasOC/blob/develop/Docs/User_Manual.pdf)

## Prototype Guide
[Prototype Guide](https://github.com/TeteV/AcampadasOC/blob/develop/Docs/Prototype_Guide.pdf)


## Usability

When we design multiplatform applications, the interface that we propose will be essential for the user to feel comfortable working with it. Bad design can cause customers to leave our application.

Within the context of computer applications and software development, usability is defined as the discipline that studies how to design websites and applications so that users can interact with them in the easiest, most comfortable and most intuitive way possible.

Among the elements that I have taken into account when trying to create a design as legible as possible and that does not provide any kind of difficulty or deficiency when interpreting its content, they have been:
  
* **Colors:** Among the colors that I have used in this design we find:
  * **#F5F6FC:** I have used this color mainly for the use of small details, such as some buttons, or in case you want to highlight small elements or icons.
  * **#00BCAA:** I have used it for the subject of titles, subtitles and some other elements in which I used it to create contrast. 
  * **#024341:** I use them mainly to create a gradient in the background of many of the windows of my interface or prototype.
  * **#F4765B:** I use it to create a little contrast between the text in that window, when the background is starring an image.
  
* **Typography:** The fonts I have used are:
  * **Ubuntu:** I use it for the titles of most items.
  * **Quicksand:** I use this typeface mainly for the description inside the rooms or for elements such as reviews. Also, he used different weights within it from "Light", "Regular" and "Medium".
  
  * **Interactive elements:** In the prototype you will find the buttons as interactive elements, in each of the majority of windows, in some, they will be a button to complete the reservas search, in others they will be arrows to move between elements of a slideshow, or in another to display the menu.

* **Data presentation:** In this case, when talking about the presentation of data, I think I should emphasize precisely the last window, which is where I specify information about the reservations, there what I do is precisely divide the content into different sections, from the location of said reservation, a brief description of how many people are going, how many cars are going to be used, etc.

## Technology Stack
The technology stack is divided into the following technologies:

* **Backend:** PHP 7 / Laravel Framework
* **Microservicio:** Lumen Framework
* **BBDD:** MySQL
* **Frontend:** Android Kotlin


## Technology Comparison
As for the technology comparison, I would like to get to the point and compare the programming in native or hybrid App's, since they are the ones I have had the pleasure of working with. As for these two, I would like to emphasize that both have their advantages and disadvantages.

Even so, I will try to expose it much more clearly:
* **Time-to-market of the initial development / add and adjust a new functionality:** In the case of hybrid applications it is not necessary to maintain two different technologies and two different base codes, so with the same resources we can produce new features faster.
* **User experience:** While it is true that with current technologies, it is possible to create a user experience very similar to a native one, in some old terminals, the performance and therefore the user experience may be somewhat lower, therefore it is common for the application to flow better when it is 100% native.
* **Simplicity / Reuse of code:** The code of a hybrid application is portable to other platforms with minimal complexity, as long as they have support for said applications (Android Auto, Android Tv, etc). In addition, the existence of a single code for the base application, despite the fact that there may be specific customizations for each platform, gives a clear advantage to the hybrid application.
* **Look & Feel of the application:** It was always easier since the compatibility with CSS, which hybrid applications have, gives it a clear advantage in this regard, it should be noted that the native ones have a large number of libraries with which to get this "Look & Feel".
* **Development cost:** In native applications, by having to generate a different code for each platform, development costs grow in proportion to the number of platforms on which the developed application will be available.

In conclusion, for me there are two key elements that we identify in almost all the projects we face: time and money. In the case of hybrid applications, both the time-to-market (initial and evolutionary) and the costs are lower, therefore it is presented as a very good option for the development of App's that do not make use of elements such as 3D graphics , super-intensive use of CPU or have a direct link with hardware elements such as thermal cameras, NFC readers, etc.

### Web Aps

At the same time, highlight the app webs. They are accessed directly from the browser of the phone or tablet, and in almost all devices it will be opened in almost the same way thanks to the use of Responsive Web Design. It is the simplest and most economical option, since it reduces development costs. Compatibility, usability and simplicity are among its advantages.

### PWAs or Progressive Web App

PWAs are commonly defined as Apps that bring together the best of web and native applications, even coming to be understood as a middle ground or an evolved form. Among its advantages we find:
* **Responsive:** Currently, most websites have a responsive design that allows them to adapt to different devices, something essential with the predominant role of smartphones.
* **Actualizada:** The PWAs will always show their latest version to the user, with the use of automatic updates, constantly and instantaneously and without the need to download them. This is possible thanks to the use of Service Workers and because it is still a web App, independent of the publication (and the entire process of revision and installation by the user that it entails) in the application markets.
* **Segura:** The secure HTTPS protocol is always used, which is also necessary for the Service Worker installation.
* **Offline:** A PWA must allow access, either partially or even totally, despite the fact that there is no Internet connection (or it is of low quality).
* **Multiplataforma:** In its development, the technology used contemplates its execution on various devices, operating systems and browsers. This, in addition to being key when it comes to offering a satisfactory user experience and reaching more potential audiences, makes it easier for developers and reduces costs, since differentiated programming is not required (something that does happen with native Apps) .
* **Indexable y enlazable:** The content of a PWA is trackable and indexable, so that it can appear as a result in a search engine. In addition, it can be shared using a URL, with the possibility that the other person uses it without installing it.

### Power Apps

On many occasions, it is for a company to find a solution that is specifically tailored to its needs and if it does find it, the time factor may be the reason that profit maximization is not achieved immediately. By integrating the Power Apps tool into your organization, you can solve this problem by creating custom applications from your company and for your company. By doing so they will enjoy benefits such as:
* **Wide availability:** The development platform, Power Apps, is available with an Office 365 subscription. This service is widely available; so you can create apps of different types to integrate them on various platforms.
* **Better data integration:** If your company uses Microsoft's Common Data Service (CDS), Power Apps will allow access to business data; from more than 200 different data sources such as Outlook, OneDrive, PowerPoint, Excel, SharePoint, Dynamics 365, Salesforce, Dropbox, among others.
* **Automated functions:** With the integration of Power Apps, companies can automate their processes and functions, leaving aside the manual entry of certain data. You can also send relevant updates and data to workers and buyers directly to their smartphones via push notifications.
* **Guaranteed security:** With the integration of Power Apps in the company, you have full control of the applications through the administration center.
* **Higher productivity:** By being able to create applications to meet the needs of the company and automate processes, greater productivity is achieved. Employees save time by not having to perform manual tasks and better optimize their time to dedicate to tasks of greater relevance to the company.

# Aditional content
once the app is running you must to create an account and login to enjoy it

## Made with
[Php Storm](https://www.jetbrains.com/es-es/phpstorm/) Website.\
[Android Studio](https://developer.android.com/studio) Website.

Readme.md template by [Villanuevand](https://gist.github.com/Villanuevand/6386899f70346d4580c723232524d35a)

---
💻 with 💜 by [TeteV](https://github.com/TeteV) 🐦 & [Javi Medina](https://github.com/JavierMedina11)
