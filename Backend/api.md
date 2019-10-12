# API specification
## Frontend --> Backend
```
{
    preferences: [
        "music:rock", "music:jazz", "art:renaissance", "conference:ai", ...
    ],
    period: {
        startDate: "2019-09-29T00:00:00",
        startDate: "2019-11-09T23:59:59"
    }
    schedule: [
        {
            start: "2019-10-12T15:05:12",
            end: "2019-10-12T18:00:00",
            recurring: False,
            location: NOT_IMPLEMENTED
        },
        {
            start: "2019-10-15T08:15:00",
            end: "2019-10-15T10:00:00",
            recurring: True,
            location: NOT_IMPLEMENTED
        },
        ...
    ]
}
```

## Backend --> Frontend
```
[
    {
        name: "Jazz concert",
        start: "2019-10-17T22:30:00",
        end: "2019-10-18T02:00:00",
        category: "music",
        location: NOT_IMPLEMENTED
    },
    {
        name: "Van Gogh exhibition",
        start: "2019-10-22T12:45:00",
        end: "2019-10-18T14:30:00",
        category: "art",
        location: NOT_IMPLEMENTED
    },
    ...
]
```

# Categories and genres
* `music`
  * `rock`
  * `metal`
  * `jazz`
  * `pop`
  * `electro`
  * `rap`
* `art`
  * `contemporary`
  * `modern`
  * `abstract`
  * `renaissance`
* `conferences`
  * `ai`
  * `blockchain`
  * `climate`
  * `architecture`
  * `microbiology`
  * `database`