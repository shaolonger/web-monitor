export interface Alarm {
    id: number;
    name: string;
    projectIdentifier: string;
    level: number;
    category: number;
    rule: string;
    startTime: string;
    endTime: string;
    silentPeriod: number;
    isActive: number;
    createTime: string;
    updateTime: string;
    createBy: number;
    isDeleted: number;
}