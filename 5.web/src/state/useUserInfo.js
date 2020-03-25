let userInfo = {};

const setUserInfo = newUserInfo => {
    userInfo = {...userInfo, ...newUserInfo};
};

const useUserInfo = () => {
    return [userInfo, setUserInfo];
};

export default useUserInfo;