## MyBnb

The purpose of this project was to create a Java Application named MyBnB for CSCC43 Introduction to Databases, for Summer 2023, lectured by Nick Koudas. MyBnB is a simple application that supports the same operations as the famous “Air BnB” service, which allows hosts to create an account, post listings and acquire bookings. Further, it allows potential renters to create an account, browse and search through many listings, and create and cancel bookings. Further, renters and hosts are allowed to leave reviews and comments for prospective renters/hosts to gain insight on a particular renter/host.


## System Description

Installing and Using Application:
This application, built using Java and MySQL, will use the terminal as the User Interface. In order to use this application, please download the source code from this GitHub link (https://github.com/ArielleRamgoolie/MyBnB). To begin, open project on an IDE such as Visual Studio Code or IntelliJ. Please run App.java to start the application. Note: This is under the assumption that the local database is linked to the application correctly and data has been loaded in bulk. Please use schema.txt to bulk load the data.

Types of Users:
MyBnB welcomes two different groups of users. Firstly, we welcome potential renters who are looking for homes, guesthouses, apartments and more to rent for a period of time. Secondly, we welcome all hosts who wish to rent out their properties for a period of time.

How to begin:
To use the application, both renters and hosts can start off by registering via our welcome menu. After this, your account is permanent, until of course, you decide in the future to delete.

Renter’s Search and Sort:
After a renter’s account is created, they have the options of exploring all listings, using our search feature. Renters can search based on distance to an entered location (longitude and latitude), address, or postal code. Renters are allowed to then further filter their previous search by sorting by prices in ascending order, descending order, filter by available dates, available amenities and by price range. We also have offer advanced search method which allows renters to specify several attributes that they are looking for in a listing such as: distance, host, type of listing, price of listing, city, country and more.

Renter’s Bookings:
Renters are allowed to enter a listing’s ID then create a booking for that listing. They will be shown the available dates and can select a start date and end date, as long as it is within the range of available dates. Renters are allowed to create as many bookings as they would like.

Renter’s Comments on Listings and Reviews on Hosts:
Renters are allowed to make comments on listings which are available for other potential renters to view. These comments however, can only be made on listings where the renter previously stayed at. This feature allows potential renters to get feedback from persons who have stayed at the listing before.

Renters Cancellations:
Renters are allowed to cancel their bookings at any time but should keep in mind that these cancellations are recorded by admins.

Host’s Listings:
When a host creates an account and logs in, they have the option of viewing all of their listings.
This can allow a host to see all the listings he’s currently showing to potential renters. Renters can view all information about their listing from this function.

Host’s Toolkit (Creating Listings):
All hosts have the options of adding one or multiple listings at any given time to the application. Our host toolkit serves the purpose of helping hosts place their listings by suggesting prices based on the average price of the listings of other listings in that city and of that type. Upon entering their city and type of listing, the host is asked to enter the price of their listing, and is also given the suggested price. Secondly, the host toolkit allows hosts to clearly see the possible price increase of their listing, if a certain amenity is added. This is done by collecting the average prices of listings with the amenity that the host does not currently have within their listing. Lastly, our host toolkit suggests which amenities should be added based on the most common amenities found on the Airbnb website.

Host’s Updated:
All hosts are allowed to update their listings. Hosts are allowed to update the price of listings but are restricted to only updating the prices for the dates that are not currently booked. In order to increase the price of a listing within a booked timeframe, hosts must first cancel this booking. Please see Host’s Cancelled Bookings for more. Hosts also have the option of marking off certain days as booked. It is expected that hosts will use this to remove certain date ranges as unavailable to renters.

Hosts Cancellations:
Hosts are allowed to cancel their bookings at any time but should keep in mind that these cancellations are recorded by admins.
Termination of Account:
Both renters and hosts are allowed to delete their accounts at any time. However, at the moment, we are only allowing renters and hosts to do so if they have no bookings within our system. Thus, if this option is selected but there are bookings, users will be asked to cancel all bookings before termination of the account.
The above are summaries of the main operations that can be performed by our users, our renters and hosts. You can find more details about these implementations by running the code, and viewing a renter’s menu and host’s menu. Please see source code for further details

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
