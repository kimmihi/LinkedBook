import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import FooterButton from "../../../components/common/Buttons/FooterButton";
import Header from "../../../components/Layout/Header";
import RecommendUserList from "../../../components/user/recommend/RecommendUserList";

import { Wrapper } from "./style";
import { request, requestGet } from "../../../api.js";

export default function RecommendUser() {
  const [userList, setUserList] = useState([]);
  const [dong, setDong] = useState("");
  const history = useHistory();

  function follow(userId) {
    const newUserList = userList.map((user) => {
      return user.userId === userId ? { ...user, isFollowing: true } : user;
    });
    setUserList(newUserList);
  }
  function unFollow(userId) {
    const newUserList = userList.map((user) => {
      return user.userId === userId ? { ...user, isFollowing: false } : user;
    });
    setUserList(newUserList);
  }

  function requestFollow() {
    const loginUserId = JSON.parse(localStorage.getItem("loginUser")).id;
    for (const user of userList) {
      if (user.isFollowing) {
        request("POST", "/follow", {
          fromUserId: loginUserId,
          toUserId: user.userId,
        });
      }
    }
  }

  function handlePassButtonClick() {
    localStorage.setItem("needRecommend", "false");
    requestFollow();
    history.push({ pathname: "/" });
  }

  function getIsFollowing(bookStar, followings) {
    for (const following of followings) {
      if (following.id === bookStar.userId) return true;
    }
    return false;
  }

  async function addFollowings(bookStars) {
    const response = await requestGet("/follow/following", {
      page: 0,
      size: 10000,
    });
    const followings = response.result.map((following) => following.user);
    const bookStarsWithFollowing = bookStars.map((bookStar) => {
      return { ...bookStar, isFollowing: getIsFollowing(bookStar, followings) };
    });
    return bookStarsWithFollowing;
  }

  async function fetchData() {
    const areas = await requestGet("/user-areas");
    const areaId = areas.result[0].areaId;
    setDong(areas.result[0].areaDongmyeonri);
    const params = {
      type: "STAR",
      areaId: areaId,
      page: 0,
      size: 10,
    };
    const response = await requestGet("/users", params);
    const result = await addFollowings(response.result);
    setUserList(result);
  }

  useEffect(() => {
    fetchData();
  }, []);
  return (
    <>
      <Header title="책방 추천" />
      <Wrapper>
        <h1>Book Star</h1>
        <h2>{dong}의 인기 책방을 팔로우 해보세요</h2>
        <RecommendUserList
          userList={userList}
          follow={follow}
          unFollow={unFollow}
        />
      </Wrapper>
      <FooterButton value="완료" onClick={handlePassButtonClick} />
    </>
  );
}