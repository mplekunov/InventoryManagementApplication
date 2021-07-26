# Inventory Management App
## General Information
## Features
- Import items to TSV file.
- Export Items from TSV file (assuming TSV file has correct format).
- Add, Edit and Remove operations.
- Sorting by Name, Price, Serial Number, Date.
- Searching Items by Name, Serial Number.

This program allows user to add, remove, edit, export and import items.
Each Item have 4 properties that can be defined by the user (3 of them have to be difined and 1 is optional)
Properies which have to be initialized are Name, Price and Serial Number (Date is optional).

Name format:
- Accepts both lowercase and upercase letters, digits, as well as all kinds of special characters.
- Names aren't unique; two or more Items can have same name.

Serial Number format:
- Serial Number must be 10 characters long.
- Serial Number must be consisted of uppercase letters or digits. No special characters are allowed.
- Serial Number must be unique.

Price format:
- Price must be consisted of only digits; floating point numbers are allowed, however, they should not have more than 2 decimal digits.
- Prices aren't unique.
- Prices are formated according to the US Currency format.

## User Guide

##### To add item, press on add button, fill all required fields and then, click on "save".
![alt text](https://i.imgur.com/Gj2TiMz.png)
##### To remove item, press right mouse button on the item and click on "delete" option shown from the drop-down menu
![alt text](https://i.imgur.com/I65MCS9.png)
##### To edit item, click on any fields that you would like to change and submit your changes by pressing enter. To discard changes, click outside of the field.
![alt text](https://i.imgur.com/6Y5DJXK.png)
##### To search for item, use "Search" field. To discard search results, clear "Search" field.
![alt text](https://i.imgur.com/pU3GQYR.png)
##### To import, press on Import button, choose file with an appropriate format and press on open that file.
![alt text](https://i.imgur.com/l703X2j.png)
##### To export, press on Export button, enter file name and press on save.
![alt text](https://i.imgur.com/GoOQfia.png)
