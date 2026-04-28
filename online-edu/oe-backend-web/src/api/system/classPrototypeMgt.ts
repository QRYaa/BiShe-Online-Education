import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsClassPrototype/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsClassPrototype/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsClassPrototype/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsClassPrototype/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsClassPrototype/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsClassPrototype/delete',
    data,
  });
}
