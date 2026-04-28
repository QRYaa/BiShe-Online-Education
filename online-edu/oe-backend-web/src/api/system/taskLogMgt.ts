import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTaskLog/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTaskLog/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTaskLog/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTaskLog/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTaskLog/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTaskLog/delete',
    data,
  });
}
