import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeOrderItem/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeOrderItem/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeOrderItem/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeOrderItem/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeOrderItem/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeOrderItem/delete',
    data,
  });
}
