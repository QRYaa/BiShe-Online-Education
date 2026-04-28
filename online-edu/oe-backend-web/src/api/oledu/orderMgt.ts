import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeOrder/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeOrder/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeOrder/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeOrder/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeOrder/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeOrder/delete',
    data,
  });
}

export function changePaymentStatus(id: number, paymentStatus: number) {
  return request.post<any>({
    url: '/admin/oeOrder/changePaymentStatus',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { id, paymentStatus },
  });
}
