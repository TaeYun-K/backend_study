import http from "k6/http";

export const options = {
  vus: 200, // Virtual Users
  duration: "2m", // Test duration
};

export default function () {
  http.get(`${__ENV.BASE_URL}/api/attractions?contentTypeId=12&page=1000&size=20`); // 관광지인 데이터
}