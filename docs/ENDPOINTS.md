# Endpoints

**Project EndPoints**


| **_endpoint_** (EN) | **_path_**(EN)                              | **_description_** (EN)                     |
|:--------------------|:--------------------------------------------|:-------------------------------------------|
| GET                 | /dashboard/status                           | show new and canceled subscriptions        |
| GET                 | /dashboard/currentRevenue/{plan}            | show revenue till now filtered by plan     |
| GET                 | /dashboard/revenuePlan/{plan}&{numberMonth} | show future plans revenue filtered by plan |
| POST                | /plans                                      | creates plan                               |
| GET                 | /plans                                      | get all existing plans                     |
| PATCH               | /plans/update/{name}                        | updates plan                               |
| PATCH               | /plans/deactivate/{name}                    | deactivate plan                            |
| PATCH               | /plans/moneyUpdate/{name}                   | update plan fees                           |
| GET                 | /plans/history/{name}                       | gets all plan fees changes                 |
| PATCH               | /plans/promote?name=                        | promotes a plan                            |
| DELETE              | /plans?name=                                | ceases a plan                              |
| GET                 | /subscriptions                              | get subscription details                   |
| PATCH               | /subscriptions                              | cancels a subscription                     |
| POST                | /subscriptions/create                       | subscribes to a plan                       |
| POST                | /device                                     | Creates a new device                       |
| PUT                 | /device/{macAddress}                        | Fully replaces an existing device          |
| GET                 | /device/all                                 | Gets all user devices                      |
| DELETE              | /device/{macAddress}                        | Deletes an existing device                 |
| GET                 | /device/photo/{fileName}                    | Downloads a photo of a device              |



















