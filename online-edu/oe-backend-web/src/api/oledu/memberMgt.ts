import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMember/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMember/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMember/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMember/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMember/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMember/delete',
    data,
  });
}
