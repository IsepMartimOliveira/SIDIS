# Problem Statement

## 1- Objective
Design and Implement the service layer of a prototype distributed system based on service orientation principles.

## 2- The Problem
**Product Reviews.**
ACME, Inc. is a SaaS-based music streaming service and wants to develop a service-oriented solution to manage the subscriptions of the service and collect some important metrics. The company offers several plans and needs the ability to create new plans to adapt to market. Currently the company offers the following plans:


![metric](/prints/metric.png)


Due to marketing reasons, the company wants to be able to promote a plan, that is, give an highlight to a certain plan, for example, by rendering it on the web page with a different layout that gives it more screen area than the others. In the previous example, the Gold plan is the promoted plan. Plans can also be
deactivated or ceased. A deactivated plan is one that no longer is available for subscription but remains inforce for current active subscribers of such plan. A plan can only be ceased if there are no current subscribers of such plan. Usually, the company migrates the subscribers to a different plan prior to ceasing it.
A customer can subscribe to the service by selecting a plan and paying for the annual or monthly fee. This entitles the customer to listen to up to n minutes of music on the number of devices according to the plan they selected. Other features are also dependent on the selected plan.
Furthermore, the company wants to know some metrics to manage the business. Namely the company needs to track:
• All the revenue generated so far
• Future cash flows based on currently active subscriptions
• Active subscriptions
• Monthly new subscriptions
• Monthly canceled subscriptions
• Churn rate
All these metrics can be viewed at the company level or broken down by plan. In the end, the company wants to produce a dashboard like the following :

![plans.png](/prints/plans.png)

In the previous sample data, all subscribers are monthly subscribers, so the monthly revenue is obtained by simply multiplying the monthly fee of each plan by the number of active subscribers. If there were annual subscriptions the revenue calculations must use the proportional monthly fee based on the annual fee. The sample data also does not consider changes in prices of the plan during the period.
The cash flow map is a timeline-based listing of the cash the company has received/will receive per month, on the last/upcoming n months. For instance, a future cash flow map, assuming the current monthly subscribers will remain, and the annual subscribers will not renew, might look the following example:

![cash](/prints/cash.png)

Since the company wants to give freedom to its marketing team to generate the media content best suited to the business it was decided that the frontend of the system will be developed using a low code or CMS solution. As such, the system being procured here is a backend service or set of services that any frontend the company decides in the future can interact to reach its business goals.
Since generating all the data needed for the dashboard might be a lengthy operation, a technical solution using asynchronous calls and/or a “summary” database updated via events, or a similar approach must be followed.
Notes:
• Payments are out of scope and can be assumed to always be performed with success by a third- party component
• The fulfilment of the actual streaming service is out of scope


## 3- Use cases Phase 1

3.1 Work Package #0 – setup
1. As admin I want to “bootstrap” user credential data
2. As admin I want to “bootstrap” plans data

3.2 Work Package #1A - Plans
1. As Marketing director, I want to define a new Plan detailing the monthly and annual cost, the maximum number of devices and other characteristics of the plan
2. As marketing director, I want to deactivate a plan
3. As marketing director, I want to change a plan’s details other than pricing
4. As a new customer I want to know all existing plans

3.3 Work Package #2A - Subscriptions
1. As a new customer I want to subscribe to a plan
2. As subscriber I want to cancel my subscription
3. As subscriber I want to know the details of my plan

3.4 Work Package #3A – Dashboard
1. As Product Manager I want to know how many new subscribers and cancelations occurred in a specific month 

3.5 Work Package #4A – Devices
1. As subscriber I want to add a new device to my subscription
2. As subscriber I want to remove a device from my subscription
3. As subscriber I want to update the details of my device (name and description)
4. As subscriber I want to list my devices

3.6 Work Package #5A – Bonus use cases
1. Subscriptions - As subscriber I want to upload an image to my profile
2. Device - As subscriber I want to upload an image when creating/editing a device

3.7 Work Package #6A – Non-functional requirements
1. OpenAPI specification 
2. Sample requests and responses, e.g., POSTMAN collection 
3. Automated tests, e.g., POSTMAN collection

## 4- Use cases Phase 2

4.1 Work Package #4B – Plans

20. As marketing director, I want to promote a plan
21. As marketing director, I want to cease a plan.

4.2 Work Package #4B – Subscriptions

22. As subscriber I want to switch my plan (upgrade/downgrade)
23. As subscriber I want to renew my annual subscription
24. As marketing director, I want to migrate all subscribers of a certain plan to a different plan

    4.3 Work Package #4B – Dashboard

25. As Product Manager or Financial director, I want to know the future cashflows for the upcoming n months filtered/broken down by plan
26. As Product Manager or Financial director, I want to know the year-to-date revenue of my company filtered/broken down by plan

    4.4 Work Package #4B – Plans

27. As marketing director, I want to change the pricing of a plan
28. As marketing director, I want to know the price change history of a plan

    4.5 Work Package #4B – Non-functional requirements

29. All authenticated requests must use JWT
30. OpenAPI specification
31. Sample requests and responses, e.g., POSTMAN collection
32. Automated tests, e.g., POSTMAN collection
33. Long result lists must support pagination

    4.6 Work Package #4B – Bonus use cases/requirements

34. Augment the subscription profile with a weather forecast based on the location of the subscriber
35. Augment the subscription profile with a funny quote of the day



## 5 - User story acceptance criteria
   All user stories have the following acceptance criteria:
   * Analysis and design documentation
   * OpenAPI specification
   * POSTMAN collection with sample requests for all the use cases with tests.
   
## 6- Working mode
1. Each team will represent a company developing the solution to a customer.
2. The professor of Theoretical classes will work as the customer
3. The professor of the Lab classes will help the team in setting up the team environment and solve
   technical difficulties
4. Even though the assessment is individual, this is a joint project. From the customer’s perspective
   there is just one project and not individual projects (one from each student). As such the team
   mentality should be “one for all, all for one”, either you all win or you all loose. Nonetheless, to
   simplify the assessment of the work you may divide the work packages among team member’s in
   the following way:

    a. Work package 0 and 5 are the responsibility of the whole team

    b. Work packages 1, 2, 3 and 4 are the responsibility of one team member
   
    c. Note that, even if you are responsible for one work package you should help your team
   members in the other work packages if they are struggling with it. Remember, “one for
   all, all for one”
   
    d. Work package 6 represents optional features and are the responsibility of the whole team


5. The project development must follow the software engineering process as explained in ESOFT and
   as such, the team must:

    a. Work iteratively

    b. Analise the requirements and engage with the customer for clarifications (do not assume
      anything; always ask the customer what he really wants)

    c. Design the overall system architecture prior to starting the development
   
    d. For each use case,

    i. start by detailing the analysis and elaborate the design of the use case justifying
      your decisions

    ii. Implement the use case taking into consideration all the best practices learnt
      throughout the course (not just this course unit) and the acceptance criteria

    iii. Automate the test of the use case (e.g., junit, POSTMAN tests)

6. Third party libraries may be used freely but their use must be justified
7. Code extracts obtained from other sources (e.g., stack exchange) must be clearly marked thru
   comments in the code indicating its origin

## 7- Logistics
1. The assignment is to be made in groups of three or four students

2. PL classes will be devoted to help the students in carrying out the assignment.
3. Presentation and assessment of the assignment will be carried out in PL classes following the due
   date.
4. Delivery of the assignment will be done thru moodle in a single ZIP file (not RAR) with
   a. Analysis and design documentation
   b. Source code tarball
   c. Provide your self-assessment and peer assessment

## 8-  Assessment
   • Assessment will be done according to the criteria table in a scale of 0 to 4 (with one decimal place) for each criterion, then converted to a scale of 0 to 20.

   • Assessment grade may be given with one decimal place

   • Grades are individual, as such each student may have a different grade from the other group
   members
