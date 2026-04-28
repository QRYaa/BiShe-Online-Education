import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeSensitiveWord/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeSensitiveWord/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeSensitiveWord/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeSensitiveWord/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeSensitiveWord/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeSensitiveWord/delete',
    data,
  });
}
