import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function userInfo() {
  return request.get<any>({
    url: '/admin/bsUser/userInfo',
  });
}
export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsUser/page',
    params,
  });
}
export function search(params: any) {
  return request.get<any>({
    url: '/admin/bsUser/search',
    params,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsUser/get/${id}`,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsUser/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsUser/update',
    data,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsUser/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsUser/delete',
    data,
  });
}
export function listAll() {
  return request.get<any>({
    url: `/admin/bsUser/listAll`,
  });
}
export function changePassword(id: number, newPassword: string) {
  return request.post<any>({
    url: '/admin/bsUser/changePassword',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { id, newPassword },
  });
}
export function changeMyPassword(oldPassword: string, newPassword: string) {
  return request.post<any>({
    url: '/admin/bsUser/changeMyPassword',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { oldPassword, newPassword },
  });
}
export function assignRole(data: any) {
  return request.put<any>({
    url: '/admin/bsUser/assignRole',
    data,
  });
}
export function addApp(userId: number, appId: number[]) {
  const appIdString = appId.join(',');
  return request.post<any>({
    url: '/admin/bsUser/addApp',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { userId, appId: appIdString },
  });
}

export function deleteApp(userId: number, appId: number) {
  return request.post<any>({
    url: '/admin/bsUser/deleteApp',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { userId, appId },
  });
}
export function findByUsername(username: any) {
  return request.get<any>({
    url: `/admin/bsUser/findByUsername?username=${username}`,
  });
}
