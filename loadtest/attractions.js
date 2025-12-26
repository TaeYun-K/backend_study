import http from "k6/http";

export const options = {
  vus: 100, // Virtual Users
  duration: "30s",
};

export default function () {
  http.get("http://localhost:8080/api/attractions?contentTypeId=12&page=0&size=20"); // 관광지인 데이터
}
