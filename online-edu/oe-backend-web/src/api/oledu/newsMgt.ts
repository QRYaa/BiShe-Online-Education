import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeNews/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeNews/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeNews/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeNews/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeNews/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeNews/delete',
    data,
  });
}
