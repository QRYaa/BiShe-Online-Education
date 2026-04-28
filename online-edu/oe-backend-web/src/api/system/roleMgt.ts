import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsRole/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsRole/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsRole/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsRole/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsRole/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsRole/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsRole/delete',
    data,
  });
}
export function rolePermission(id: any) {
  return request.get<any>({
    url: `/admin/bsRole/rolePermission/${id}`,
  });
}
export function assignRolePermission(data: any) {
  return request.post<any>({
    url: '/admin/bsRole/assignRolePermission',
    data,
  });
}
