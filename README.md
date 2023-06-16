![banner_CC](https://www.linkpicture.com/q/Copy-of-Obesifix-Bangkit-2023-3_1.jpg)


# Obesifix - Mobile Development
Hello, this is mobile dev Obesifix application made by Capstone Team C23-PS344

# Table of Contents
- [Introduction](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#cloud-computing-team)
- [MD Team](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#cloud-computing-team)
- [What We Do?](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#what-we-do)
- [Screenshots Application](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#screenshots-application)
- [Architecture](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#architecture)
- [Repositories](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#repositories)
- [Obesifix App](https://github.com/Obesifix-Bangkit-2023/Mobile_Development#obesifix-app)

# Mobile Development Team

|  Name | Bangkit ID | Contacts |
| ------------ | ------------ | ------------ |
| Muhammad Ilham El Hakim | A066DSX1043		 | [Github](https://github.com/ilham-ha1) & [Linkedin](https://www.linkedin.com/in/muhammad-ilham-el-hakim-0764a6169/)  |
| Muchammad Raharjo Waluyo	 | A036DKX3836		| [Github](https://github.com/mhrarw) & [Linkedin](https://www.linkedin.com/in/muchammad-raharjo-waluyo-81b844158/) |

# What We Do?
We make a UI design, starting from a paper prototype and turning it into a mockup. Then we sliced those designs into Android XML Layout form. Next, we integrate it with the API endpoint to use authentication, food recommendations using the ML model, and access food data to apply it for each feature.

# Screenshots Application

![Screenshots](https://www.linkpicture.com/q/Screenshot-2173.png)

![Screenshots](https://www.linkpicture.com/q/Screenshot-2172_1.png)

# Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.
![Architecture](https://www.linkpicture.com/q/Screenshot-2175.png)

# Repositories

|   Learning Paths       |                                Link                                              |
| :----------------:     | :----------------------------------------------------------------:               |
|   Organization         |            [Github](https://github.com/Obesifix-Bangkit-2023)                    |
|  Machine Learning      |            [Github](https://github.com/Obesifix-Bangkit-2023/Machine_Learning)   |
|  Machine Learning API  |        [Github](https://github.com/Obesifix-Bangkit-2023/ML-services-API)        |
| Mobile Development     |            [Github](https://github.com/Obesifix-Bangkit-2023/Mobile_Development) |


# Obesifix App
    .com.c23ps344.obesifix-app  # Roor Package
    ├── adapter                     # Adapter for RecyclerView
    │
    ├── data                        # For data handling
    │   ├── dao                     # For local bookmark feature
    │   ├── database                # Store data entity locally     
    |   ├── repository              # Single source of data     
    │   └── paging-source           # For paging3 handling
    |
    ├── network                     # Remote Data Handlers
    │   ├── api                     # Retrofit API for remote endpoint
    │   └── responses               # Store data entity remote
    │
    ├── ui                          # Activity/View layer
    │   ├── custom-view             # Text validation handlers
    │   ├── fragment                # View for Navigation
    │   ├── activity                # View for Splash and Main
    │   └── view-model              # ViewHolder for RecyclerView
    |
    └── utils                       # Utility Classes / Kotlin extensions