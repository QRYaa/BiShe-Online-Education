import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsUserAppLog/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsUserAppLog/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsUserAppLog/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsUserAppLog/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsUserAppLog/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsUserAppLog/delete',
    data,
  });
}
