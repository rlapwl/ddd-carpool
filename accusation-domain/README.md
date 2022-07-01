## DDD-Carpool

## 신고관리 서비스

### 신고등록 API

#### URL

- ```post``` /accusation

#### Request

```요청 항목```

| 이름          | 타입   | 설명                          |
| ------------- | ------ | ----------------------------- |
| accusedUserId | long   | 신고자 ID                     |
| attackerId    | long   | 신고대상 ID                   |
| attackerRole  | enum   | 신고대상 역할 (운전자/카풀러) |
| title         | String | 신고 제목                     |
| desc          | String | 신고 내용                     |

```요청 예시```

```json
{
    "accusedUserId": "1",
    "attackerId": "2",
    "attackerRole": "DRIVER",
    "title": "test",
    "desc": "test"
}
```

#### Response

```HTTP Header```

```
StatusCode: 201
Location: /accusation/{id}
```



### 신고 내용 조회 API

#### URL

- ```get``` /accusation/{id}

#### Request

```요청 항목```

| 이름 | 타입 | 설명                     |
| ---- | ---- | ------------------------ |
| id   | long | 신고 내용 등록시 생긴 ID |

```요청 예시```

```sh
curl -X GET localhost:8080/accusation/1
```

#### Response

```응답 항목```

| 이름          | 타입   | 설명                          |
| ------------- | ------ | ----------------------------- |
| id            | long   | 신고 내용 ID                  |
| accusedUserId | long   | 신고자 ID                     |
| attackerId    | long   | 신고대상 ID                   |
| attackerRole  | enum   | 신고대상 역할 (운전자/카풀러) |
| title         | String | 신고 제목                     |
| desc          | String | 신고 내용                     |
| createdAt     | String | 등록 시간                     |

```응답 예시```

```json
{
    "id": 1,
    "accusedUserId": 1,
    "attackerResponse": {
        "attackerId": 2,
        "attackerRole": "DRIVER"
    },
    "contentsResponse": {
        "title": "test",
        "desc": "test"
    },
    "createdAt": "2022-07-01 11:36:20"
}
```



### 

### 
