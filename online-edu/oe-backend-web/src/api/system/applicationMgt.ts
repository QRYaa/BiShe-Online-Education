import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsApplication/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsApplication/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsApplication/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsApplication/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsApplication/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsApplication/delete',
    data,
  });
}
export function listAll() {
  return request.get<any>({
    url: `/admin/bsApplication/listAll`,
  });
}
export function changeSecret(id: number, secret: string) {
  return request.put<any>({
    url: '/admin/bsApplication/changeSecret',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { id, secret },
  });
}
export function findByUserId(userId: number) {
  return request.get<any>({
    url: `/admin/bsApplication/findByUserId?userId=${userId}`,
  });
}
