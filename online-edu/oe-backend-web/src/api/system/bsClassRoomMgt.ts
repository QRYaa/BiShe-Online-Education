import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsClassRoom/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsClassRoom/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsClassRoom/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsClassRoom/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsClassRoom/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsClassRoom/delete',
    data,
  });
}
