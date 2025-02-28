
# Ashena

Ashena is a contact manager app that help users to manage their contacts (Both in device contacts and in-app contacts)  and their related informations. Ashena project developed by the purpose of simplicity and respecting users privacy, thus the only permission it asks is **Accessing Contacts** and if user decide to don't grant the permission the app still serve user by in app contacts.
## Tech Stack

**Architecture:** MVVM

**Reactive Programming:** Kotlin Coroutines, Flow and LiveData

**UI:** XML Views

**Dependency Injecttion:** Hilt

**Database:** Room
## Features

- Light/dark mode
- User frindly UI
- minimum permissions needed
- Reactive and seamless interactions


## Lessons Learned

Like any other projects and trials i encountred new challanges like:

**1. Combining both in-app and device contacts in a list:**

In the scenario that users granted the contacts permission and also has a number of in-app contacts, i had to present both of these contacts type in a single list. i overcome this challange by usinmg Flow and FlatMap operator.


**2. Fetching Device Contacts:**




**3. Handling Runtime Permissions:**


## Screenshots

![Page 1](Screenshots/Ashena%20Presentation_Page_2.png)
![Page 2](Screenshots/Ashena%20Presentation_Page_3.png)
![Page 3](Screenshots/Ashena%20Presentation_Page_4.png)
![Page 4](Screenshots/Ashena%20Presentation_Page_5.png)
![Page 5](Screenshots/Ashena%20Presentation_Page_6.png)
![Page 6](Screenshots/Ashena%20Presentation_Page_7.png)

## Related

Here are some related projects

[Awesome README](https://github.com/matiassingers/awesome-readme)

[Contacts Reborn](https://vestrel00.github.io/contacts-android/)

[PermissionX](https://github.com/guolindev/PermissionX)

[Avatar View for Android](https://github.com/GetStream/avatarview-android)