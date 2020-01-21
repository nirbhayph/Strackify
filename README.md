# Strackify (Built for Android)

- This repository contains the work done for developing the Strackify (Sports Tracking) using data obtained from the SportsDB API. 

- This project is aimed at providing users with a way to manage a number of their favorite team's events at the switch of a click, just like you would in a story now a days present on any social platform. Users are given options to manage their favorites by selecting from number of sports teams spread out around the world. You will be able to view highlight and upcoming events for the team. This app is in the idea stage. Ideas, criticisim, suggestions are always welcome. Feel free to provide feedback on the play store. 

- Here are some screenshots from the app. 

![Sign In]()
![Walkthrough1]()
![Walkthrough2]()
![Walkthrough3]()
![Walkthrough4]()
![Walkthrough5]()
![Past Events]()
![Upcoming Events]()
![About Team 1]()
![About Team 2]()
![Select Sport]()
![Select Country]()
![Select Team]()
![Settings]()


- The API is available at https://www.thesportsdb.com/api.php

- Play Store Link - 

- Landing Page Link - 

- Link to Project Structure - https://nirbhay.me/Strackify/structure.html

- Link to JavaDoc - https://nirbhay.me/Strackify/javadoc/

- Link to Trello - https://trello.com/b/os8Qecfb/strackify

## Libraries Used 
- Firebase Auth (@firebase) - For authentication purposes 
- Volley (@volley) - For data request purposes from api (json and bitmap)
- Gson (@gson) - For storing objects as json strings in shared preferences string set
- Circle Image View (@civ) - For showing logos in circular image views
- App Intro (@appintro) - For showing the app introductory walkthrough
- Emoji Twitter (@emoji) - For showing emoji text in countries list

## Other Commons Used
- Recycler View
- Card View
- Bottom Navigation 

## Design 
- Material design 

## Details about the application 
- The project has one home activity, which is the crux of the application comprising of the past events, upcoming events, about team and settings fragment. 
- In addition to this there is the sign in, splash, intro, sport, country and finally team selection activity, which help the user with the walkthrough and selection of favorites to display on the home activity mentioned above. 
- There are search options placed for the user wherever possible. 
- Unsplash is used in the app when images are not received from the api. Random keywords relating to the event or sport are used to display the images. 
- Volley has been used for requesting data from the api. 
- MVVM pattern has been used within the home activity class by creating observables with the View Model class for a fragment. 
- The code has been packaged into 5 main packages (adapter, authentication, ui, utility, model). You can see the project structure to get a detailed idea of that. 
- A JavaDoc has been provided for the project. 

## Note 
For developers out there, if you wish to contribute to the project, feel free to do so. 

## License
This project is licensed under the MIT License - see the LICENSE.md file for details

## Developer:
- @nirbhayph - https://github.com/nirbhayph | https://linkedin.com/in/nirbhaypherwani | https://nirbhay.me

## Acknowledgements and Mentions:

- @firebase - https://firebase.google.com
- @volley - https://github.com/google/volley
- @gson - https://github.com/google/gson
- @civ - https://github.com/hdodenhof/CircleImageView
- @appintro - https://github.com/AppIntro/AppIntro
- @emoji - https://github.com/vanniktech/Emoji
- @unsplash - https://unsplash.com/
- @snap2html - https://www.rlvision.com/snap2html/about.php
- @sportsdb - https://www.thesportsdb.com
