let userInfo = {};

const setUserInfo = newUserInfo => {
    userInfo = Object.assign(userInfo, newUserInfo);
};

const useUserInfo = initUserInfo => {
    if (initUserInfo) userInfo = Object.assign(userInfo, initUserInfo);
    return [userInfo, setUserInfo];
};

export default useUserInfo;