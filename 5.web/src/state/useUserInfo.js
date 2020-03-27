let userInfo = {};

const setUserInfo = newUserInfo => {
    userInfo = Object.assign(userInfo, newUserInfo);
};

const useUserInfo = () => {
    return [userInfo, setUserInfo];
};

export default useUserInfo;