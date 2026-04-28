import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsStudent/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsStudent/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsStudent/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsStudent/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsStudent/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsStudent/delete',
    data,
  });
}
export function changePassword(id: number, newPassword: string) {
  return request.post<any>({
    url: '/admin/bsStudent/changePassword',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { id, newPassword },
  });
}
