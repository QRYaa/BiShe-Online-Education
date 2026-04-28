import { request } from '@/utils/request';

export function tree(params: any) {
  return request.get<any>({
    url: '/admin/bsDepartment/tree',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsDepartment/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsDepartment/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsDepartment/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsDepartment/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsDepartment/delete/${id}`,
  });
}
