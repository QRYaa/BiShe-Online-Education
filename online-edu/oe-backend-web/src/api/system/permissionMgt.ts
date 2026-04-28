import { request } from '@/utils/request';

export function tree(params: any) {
  return request.get<any>({
    url: '/admin/bsPermission/tree',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsPermission/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsPermission/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsPermission/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsPermission/delete/${id}`,
  });
}
