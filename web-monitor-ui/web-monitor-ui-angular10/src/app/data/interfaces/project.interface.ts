export interface Project {
    id: number;
    projectName: string;
    projectIdentifier?: string;
    description?: string;
    accessType: string;
    activeFuncs?: string;
    isAutoUpload: number;
    createTime: string;
    updateTime: string;
    userList: string;
}