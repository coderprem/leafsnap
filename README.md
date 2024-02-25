# 2024 GDSC Solution Challenge - LeafSnap

# ⚠️ **Problem**
The challenge at hand is the disconnection between youth and the environment, leading to lack of active participation in sustainable practices like tree plantation. 
People lack a shared platform to engage with the growth and well-being of trees, hindering the development of collective commitment to environmental sustainability. LeafSnap aims to bridge this gap by providing a user-friendly and interactive platform that encourages individuals to connect, share and contribute to the growth of trees.

# 🎯 **Targeted UN SDG's**

<p align="center">
  <img src="https://github.com/ibankang/leafsnap/assets/111854504/011027ee-99ce-441d-b2cc-d823811a837f" width="250" height="250" title="Zero Hunger">

  <img src="https://github.com/ibankang/leafsnap/assets/111854504/99165e38-480a-45a1-bf4a-937a937072e3" width="250" height="250" title="Climate Action">
</p>

We are using Goal (SDG) 2 and its Target 2.5, along with SDG 13 and its Target 13.2 for LeafSnap.
### SDG 2 - End hunger, achieve food security, and promote sustainable agriculture:  
**Target 2.5:** Maintain the genetic diversity in food production:  
Trees contribute to biodiversity, and their inclusion aligns with the goal of sustaining genetic diversity in food production.
   
### SDG 13 - Take urgent action to combat climate change and its impacts:  
**Target 13.2:** Integrate climate change measures into policy and planning:  
Trees are powerful tools for climate change mitigation and adaptation. By integrating them into the LeafSnap initiative, we aim to contribute to climate change resilience, demonstrating the importance of trees in policy, planning, and combating climate change impacts.
The inspiration lies in the crucial role trees play in addressing global challenges related to hunger, nutrition, sustainable agriculture, and climate change. It addresses the issues like ending hunger, achieving food security, improving nutrition and promoting sustainable agriculture while also engaging the user with daily tasks and targets and recommending them plants they can grow based on the temperature and humidity range of the region.

# ❓ **What is LeafSnap**
<p align="center">
  <img src="https://github.com/ibankang/leafsnap/assets/111854504/fae7e0d7-f21a-42ff-8a2f-1cf190ac2b55" width="250" height="250" title="Zero Hunger">
</p>
LeafSnap is designed to counter the growing disconnect between youth and environmental engagement. The platform fosters a connected and eco-conscious community, urging individuals to connect, share, and actively contribute to tree growth. It serves as a bridge between people and nature, inspiring a collective commitment to sustainable practices, particularly tree planting.

# 🤖 LeafSnap- Overview
<p align="center">
  <img src="https://github.com/ibankang/leafsnap/assets/111854504/d37fb595-2c39-4160-92a9-c58bcfef1901" width="1920" height="500" title="LeafSnap App">

  <img src="https://github.com/ibankang/leafsnap/assets/111854504/3cb10d5c-ff5f-404b-9aeb-f8080ec009a2" width="1920" height="500" title="LeafSnap- Add new plant">

  <img src="https://github.com/ibankang/leafsnap/assets/111854504/53ffeda3-ff02-4624-9ac1-214db84982b3" width="1920" height="500" title="LeafSnap- Profile">
</p>

# 🛠️ **System Architecture**
### User Interface (UI):  
The UI is the front-end component visible to users. It encompasses screens for capturing tree images, displaying growth progress, and interacting with the community. It also provide user with useful insights on which plants they can grow based on the temperature and humidity. It provides a user-friendly interface for seamless navigation and engagement.
### Mobile App Logic:  
This component handles the logic of the LeafSnap mobile application. It includes functionalities for capturing and uploading tree images, processing user interactions, and managing the overall user experience.
### Back-end Server:  
The back-end server manages the business logic, handles data processing, and communicates with external services like firebase. It is responsible for storing and retrieving user data, managing image recognition requests and admin panel, and facilitating communication between the mobile app and the database.
### Python for Recommendation:  
By implementing the recommendation system using Python, leveraging its powerful data processing and analysis libraries such as PANDAS. Python provides a flexible and efficient environment for handling the dataset, filtering plants based on user input, and generating recommendations.
### Firebase Database:  
Firebase serves as the primary database, storing user profiles, tree growth data, and community interactions in real-time. It ensures data consistency across devices and facilitates seamless updates and synchronization.

# 🛰️ **Technologies Used**
<p align="center">
  <img src="https://github.com/ibankang/leafsnap/assets/111854504/4cb2f07c-b41b-4fc9-9cfd-f786a4d49ec2" width="1920" height="450" title="Zero Hunger">
</p>

# 🏅 **Success and Completion of the Solution**
### Cause:  
There is a lack of interest, especially among young adults, in plant growth, trees, and environmental sustainability. Many individuals are preoccupied with the virtual world and are not fully aware of the adverse effects of climate change.

### Effect:  
- The project aims to encourage people to plant trees in a fun and informative way.  
- This community is nurtured by encouraging individuals to actively participate in tree planting and nurturing activities.  
- This not only facilitates the exchange of experiences but also inspires and motivates users to contribute to environmental safety.  
#### To evidence the goals of your solution, we are tracking various KPIs, like:  
- User Engagement Metrics: Tracking the number of users actively participating in tree planting activities. Measure user interactions, such as likes, comments, and shares on shared tree growth journeys.
Community Growth: Monitor the growth of user community within the app.Track the number of new users joining the platform.
- Tree Planting Initiatives: Record the number of trees planted through the app.Measure the geographical spread of tree planting initiatives.
#### To understand the impact of solution:  
- Analytics Platforms: Tracks user behavior, engagement, and conversion on the app and website.
- Offers real-time insights into user behavior, demographics, and user journeys within the app.

# 🏃‍♂️ Feedback
We asked some students, our faculty adviser and other people to test the app and give us an honest feedback to what do they think about the app and our vision. We got some interesting feedback, some positive and some constructive. Here are 3 feedback points and what we did to improve the app based on the feedback:
#### 1. “More gaming elements, like quizzes or challenges, could make the app even more engaging."  
**Improvement:** We added a social feature allowing users to create profiles, connect with fellow plant lovers, and share their plant journeys. It’s an explore feed on which user can see posts uploaded by their fellow users.
#### 2. “It would be great if the app can recommend me some plants to grow based on the current weather.”
**Improvement:** Implemented dynamic plant recommendations based on real-time weather data obtained from an external API. Users can now receive personalized plant suggestions tailored to their current weather conditions, enhancing the relevance and practicality of the app's recommendations. 
#### 3. "An option for language preferences could make the app more accessible to a broader audience."  
**Improvement:** We are working on expanding the language option for the app to improve the ease of use by the user. We are also working on having a chatbot on the website which would give 24/7 user support in the user’s preferred language

# 🌊 Scalability
### Educational Integration:  
Collaborate with schools to integrate LeafSnap into environmental science or biology curricula. Design lesson plans that involve tree planting initiatives, using the app to track progress.
### Google Maps Integration:  
This component integrates Google Maps to provide users with a visual representation of the geographic distribution of trees. It allows users to explore the community's planted trees on an interactive map.
### Schoolwide Initiatives:  
Extend the use of LeafSnap beyond classrooms to involve the entire school community. Organize tree planting days and use the app to document the collective impact of these initiatives.
### Continuous Improvement:  
Regularly update the app with new features and improvements based on user feedback and technological advancements.
### Chatbot:  
Currenty working on training a chatbot for the website. Chatbots can provide immediate responses to user queries, offering 24/7 support without the need for human intervention. Users can get quick answers to frequently asked questions, improving customer satisfaction.  

Implement analytics to track user engagement, popular tree species, and overall community impact to inform future enhancements.

# 👨‍👩‍👦 Contributors
<p align="center">
  <img src="https://github.com/ibankang/leafsnap/assets/111854504/11c43721-8cfd-4227-a037-efe8144dd6d5" width="1920" height="500" title="Team">
</p>

# 🎥 Demo Video
- Youtube: https://youtu.be/ahkGC7tghUg
# Website
If you have any questions or feedback, feel free to reach out to us at eribankang@gmail.com or [LeafSnap](https://leafsnap.ibankang.com/)
