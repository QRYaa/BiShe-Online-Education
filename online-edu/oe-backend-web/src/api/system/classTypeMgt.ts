import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsClassType/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsClassType/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsClassType/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsClassType/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsClassType/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsClassType/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsClassType/delete',
    data,
  });
}
